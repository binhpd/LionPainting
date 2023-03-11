package com.hiccup.kidpainting.activities

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.os.Handler
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.ViewTreeObserver
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.transition.Transition
//import com.google.android.gms.ads.AdListener
//import com.google.android.gms.ads.AdRequest
//import com.google.android.gms.ads.InterstitialAd
//import com.google.android.gms.ads.reward.RewardItem
import com.hiccup.kidpainting.BuildConfig
import com.hiccup.kidpainting.R
import com.hiccup.kidpainting.adapters.ColorBoardAdapter
import com.hiccup.kidpainting.adapters.ColorBoardListener
import com.hiccup.kidpainting.databinding.ActivityCollectionBinding
import com.hiccup.kidpainting.databinding.ActivityDrawingBinding
import com.hiccup.kidpainting.drawinghelper.BasePath
import com.hiccup.kidpainting.drawinghelper.DrawingView
import com.hiccup.kidpainting.drawinghelper.DrawingViewBase
import com.hiccup.kidpainting.drawinghelper.menu.MenuPainter
import com.hiccup.kidpainting.drawinghelper.menu.MenuPainterImpl
import com.hiccup.kidpainting.drawinghelper.paint.EraserPaint
import com.hiccup.kidpainting.drawinghelper.paint.PaintFactory
import com.hiccup.kidpainting.drawinghelper.replay.BaseLoader.LoadListener
import com.hiccup.kidpainting.drawinghelper.replay.ReplayDrawingSequence
import com.hiccup.kidpainting.drawinghelper.replay.SaveStateTask
import com.hiccup.kidpainting.drawinghelper.replay.StorageStateLoader
import com.hiccup.kidpainting.image.CustomTarget
import com.hiccup.kidpainting.models.ColorModel
import com.hiccup.kidpainting.models.PathData
import com.hiccup.kidpainting.models.PhotoModel
import com.hiccup.kidpainting.pref.AppPurchase
import com.hiccup.kidpainting.utilities.*
import com.hiccup.kidpainting.utilities.extension.viewBinding
import com.hiccup.kidpainting.utilities.tracking.FireBaseEvent
import com.hiccup.kidpainting.utilities.tracking.FireBaseTracker
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


/**
 * Created by hiccup on 26/08/2015.
 */
class DrawingActivity : BasePaintingActivity(), View.OnClickListener, MenuPainterImpl, ColorBoardListener {
    companion object {
        private const val STORAGE_PERMISSION = 1111
        private const val SAVE_STATE_STORAGE_PERMISSION = 1112
        const val EXTRA_PHOTO = "EXTRA_PHOTO"
        const val EXTRA_COLLECTION_NAME = "EXTRA_COLLECTION_NAME"
        const val DEFAULT_COLOR_INDEX = 5
        const val MILISECONDS_PER_SECOND = 1000

        @JvmField
        val TAG = "DrawingActivity"

        private fun saveToExternalStorage(context: Context, name: String, bitmapImage: Bitmap) {
            val helper = ExternalStorageHelper()
            if (helper.isExternalStorageWritable()) {
                val saved = helper.saveFileToExternalStorage(context, name, bitmapImage)
                if (null != saved) {
                    val intent = Intent(context, PreviewPhotoActivity::class.java)
                    intent.putExtra(DrawingActivity.EXTRA_PHOTO, saved)
                    context.startActivity(intent)
                } else {
                    LogUtils.e("Lưu thất bại")
                }
            }

        }
    }

    private var mCurrentColor: Int = 0
    private lateinit var mColorList: ArrayList<ColorModel>
    private lateinit var mMenuPainter: MenuPainter
    private var mPhotoModel: PhotoModel? = null
    private var mCollectName: String? = null

    private var mPainter: ReplayDrawingSequence? = null


//    private lateinit var mInterstitialAd: InterstitialAd

    private var rewardCount = 0

    override val binding by viewBinding {
        ActivityDrawingBinding.inflate(layoutInflater)
    }

    override fun onInitValue() {
        super.onInitValue()

        initInterstitialAds()
        loadAds()
        binding.ibSave.setOnClickListener(this@DrawingActivity)
        binding.ibHome.setOnClickListener(this@DrawingActivity)
        binding.ibUndo.setOnClickListener(this@DrawingActivity)
        binding.ibReplay.setOnClickListener(this@DrawingActivity)
        if (BuildConfig.DEBUG) {
            binding.icExport?.setOnClickListener(this@DrawingActivity)
        }

        initColorBoard()

        initMenuPaint()

        initDrawingView()

        FireBaseTracker.sendEvent(FireBaseEvent.DRAWING_OPEN)
    }

    private fun initMenuPaint() {
        mMenuPainter = MenuPainter(findViewById(R.id.root))
        mMenuPainter.setListener(this)
    }

    private fun initColorBoard() {
        mColorList = ArrayList()
        val colors = ColorHelper().getColors()
        for (color in colors) {
            mColorList.add(ColorModel(Color.parseColor(color)))
        }
        val adapter = ColorBoardAdapter(this, mColorList)
        adapter.listener = this
        // bind default color
        adapter.setSelectedIndex(DEFAULT_COLOR_INDEX)
        mCurrentColor = mColorList[DEFAULT_COLOR_INDEX].color

        if (UIUtils.isTablet(this)) {
            binding.rvColorBoard.layoutManager = LinearLayoutManager(this, RecyclerView.HORIZONTAL, false)
        } else {
            binding.rvColorBoard.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        }

        binding.rvColorBoard.adapter = adapter
        binding.rvColorBoard.scrollToPosition(0)
    }

    override fun onResume() {
        super.onResume()
        playAudio()
    }

    override fun onStop() {
        super.onStop()
        pauseAudio()
        if (mPainter != null) {
            mPainter!!.stop()
        }

    }

    override fun onBackPressed() {
        checkPermissionForSaveState()
    }

    private fun saveState() {
        if (mPhotoModel != null) {
            val task = SaveStateTask(this,
                    getName(),
                    binding.drawingView.allPath, binding.drawingView.width, binding.drawingView.height)
            task.execute()
        }
    }

    private fun initDrawingView() {
        if (!(intent == null || intent.extras == null || !intent.extras!!.containsKey(EXTRA_PHOTO))) {
            mPhotoModel = intent.extras!!.getSerializable(EXTRA_PHOTO) as PhotoModel
            mCollectName = intent.extras!!.getString(EXTRA_COLLECTION_NAME)
        } else {
            binding.ivMarker.visibility = View.GONE
            mMenuPainter.onClick(binding.ivPaintBrush)
        }
        addPaintToDrawingView()

        binding.drawingView.viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
            @SuppressLint("NewApi")
            override fun onGlobalLayout() {
                // disable this feature this release
//                if (mPhotoModel != null) {
//                    loadSaveState()
//                }
                loadImage(binding.drawingView.width, binding.drawingView.height)
                binding.drawingView.viewTreeObserver.removeOnGlobalLayoutListener(this)
            }
        })
    }

    private fun loadImage(width: Int, height: Int) {
        if (mCollectName == null) {
            return
        }

        Glide.with(this)
                .asBitmap()
                .load(AppConstants.ASSET_PATH + mCollectName + "/" + mPhotoModel!!.name)
                .into(object : CustomTarget<Bitmap>(width, height) {
                    override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                        binding.ivSamplePhoto.setImageBitmap(resource)
                        binding.drawingView.setOriginBitmap(resource)
                    }
                })
    }

    private fun loadSaveState() {
        binding.drawingView.setMode(DrawingViewBase.PREVIEW_MODE)
        val stateLoader = StorageStateLoader(this@DrawingActivity, getName(), binding.drawingView.width, binding.drawingView.height)
        stateLoader.listener = object : LoadListener {
            override fun onLoadSuccess(result: List<BasePath>?) {
                if (result != null) {
                    binding.drawingView.setAllPaths(result)
                    binding.drawingView.refresh()
                }
                binding.drawingView.setMode(DrawingViewBase.NORMAL_MODE)
            }

        }
        stateLoader.execute()
    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId

        return if (id == R.id.action_settings) {
            true
        } else super.onOptionsItemSelected(item)

    }

    override fun onClick(view: View) {
        when (view.id) {
            R.id.ibSave -> {
                checkWritePermission()
                FireBaseTracker.sendEvent(FireBaseEvent.DRAWING_SAVE)
                SoundEffect.instance.playSound(R.raw.capture_picture)
            }

            R.id.ibUndo -> {
                FireBaseTracker.sendEvent(FireBaseEvent.DRAWING_UNDO)
                binding.drawingView.undo()
                SoundEffect.instance.playSound(R.raw.tap)
            }

            R.id.ibHome -> {
                SoundEffect.instance.playSound(R.raw.tap)
                onBackPressed()
            }

            R.id.ibReplay -> {
                SoundEffect.instance.playSound(R.raw.tap)
                onClickReplay()
            }

            R.id.ic_export -> {

            }
        }
    }

//    override fun onRewarded() {
//        super.onRewarded(reward)
//        RewardHelper.getInstance().setRewardCollection(reward.amount)
//        replay()
//    }

    private fun onClickReplay() {
        if (binding.drawingView.allPath != null && binding.drawingView.allPath.size == 0) {
            return
        }

        if (isPendingReplay){
            return
        }

        if (mPainter != null && !mPainter!!.isFinished) {
            return
        }

        if (rewardCount == 1) {
            loadAds()
        }

        if (!AppPurchase.instance.isHasPurchase(this) && rewardCount == 0) {
            showInterstitial()
        } else {
            replay()
        }
    }

    private fun openStore() {
        val intent = Intent(this@DrawingActivity, LockKidActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_CLEAR_TOP
        startActivity(intent)
    }

    private fun getName() = mPhotoModel?.name?.replace(".png", "")!!

    private fun checkWritePermission() {
        val permission = ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)

        if (permission != PackageManager.PERMISSION_GRANTED) {
            makeRequest(STORAGE_PERMISSION)
        } else {
            checkAndSave()
        }
    }

    private fun checkPermissionForSaveState() {
        val permission = ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)

        if (permission != PackageManager.PERMISSION_GRANTED) {
            makeRequest(SAVE_STATE_STORAGE_PERMISSION)
        } else {
            saveState()
            finish()
        }
    }

    private fun checkAndSave() {
        if (mPhotoModel != null) {
            saveToExternalStorage(this, mPhotoModel!!.name, convertToBitmap(binding.groupDrawingView))
        } else {
            val dateFormat = SimpleDateFormat("yyyyMMddhhmmss")
            saveToExternalStorage(this, dateFormat.format(Date()) + ".png", convertToBitmap(binding.groupDrawingView))
        }
    }

    private fun makeRequest(requestCode: Int) {
        ActivityCompat.requestPermissions(this,
                arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                requestCode)

    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            STORAGE_PERMISSION -> {
                if (grantResults.isEmpty() || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                } else {
                    checkAndSave()
                }
            }

            SAVE_STATE_STORAGE_PERMISSION -> {
                if (grantResults.isEmpty() || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                } else {
                    saveState()
                }
            }
        }
    }

    private fun convertToBitmap(v: View?): Bitmap {
        val b = Bitmap.createBitmap(v!!.measuredWidth, v.measuredHeight, Bitmap.Config.ARGB_8888)
        val c = Canvas(b)
        v.layout(v.left, v.top, v.right, v.bottom)
        v.draw(c)
        return b
    }


    override fun getAudioSrc(): Int {
        return R.raw.drawing_audio
    }

    override fun onBrushChange(brushType: Int) {
        val paint = PaintFactory.getPaint(this, brushType)
        if (brushType == PaintFactory.ERASE_PAINT) {
            binding.rvColorBoard.visibility = View.GONE
        } else {
            paint.color = mCurrentColor
            binding.rvColorBoard.visibility = View.VISIBLE
        }
        trackingPaint(brushType)
        binding.drawingView.paint = paint
        // update drawing mode

        updateDrawingMode(brushType)
    }

    private fun updateDrawingMode(brushType: Int) {
        if (brushType == PaintFactory.MARKER_PAINT) {
            binding.drawingView.setMode(DrawingViewBase.BRUSH_MODE)
        } else {
            binding.drawingView.setMode(DrawingViewBase.NORMAL_MODE)
        }
    }

    private fun trackingPaint(paint: Int) {
        when (paint) {
            PaintFactory.BRUSH_PAINT -> {
                FireBaseTracker.sendEvent(FireBaseEvent.DRAWING_PAINT_BRUSH)
            }
            PaintFactory.ERASE_PAINT -> {
                FireBaseTracker.sendEvent(FireBaseEvent.DRAWING_PAINT_ERASE)
            }
            PaintFactory.MARKER_PAINT -> {
                FireBaseTracker.sendEvent(FireBaseEvent.DRAWING_PAINT_MARKER)
            }
            PaintFactory.PENCIL_PAINT -> {
                FireBaseTracker.sendEvent(FireBaseEvent.DRAWING_PAINT_PENCIL)
            }
        }
    }

    override fun onSelectColorItem(view: View, color: Int) {
        SoundEffect.instance.playSound(R.raw.tap_color)
        mCurrentColor = color
        addPaintToDrawingView()
        FireBaseTracker.sendEvent(FireBaseEvent.DRAWING_SELECT_COLOR)
    }

    private fun addPaintToDrawingView() {
        val paint = PaintFactory.getPaint(this, mMenuPainter.getCurrentPaintType())
        if (paint !is EraserPaint) {
            paint.color = mCurrentColor
        }
        binding.drawingView.paint = paint
    }

    private fun initInterstitialAds() {
//        mInterstitialAd = InterstitialAd(this).apply {
//            adUnitId = getString(R.string.ads_unit_id_interstitial)
//            adListener = (object : AdListener() {
//                override fun onAdClosed() {
//                    rewardCount = 5
//                    replay()
//                }
//            })
//        }
    }

    private fun loadAds() {
//        if (!mInterstitialAd.isLoading && !mInterstitialAd.isLoaded) {
//            var adRequest: AdRequest = if (BuildConfig.DEBUG) {
//                AdRequest.Builder()
//                        .addTestDevice(TEST_DEVICE)
//                        .build()
//            } else {
//                AdRequest.Builder()
//                        .build()
//            }
//
//            mInterstitialAd.loadAd(adRequest)
//        }
    }

    private fun showInterstitial() {
//        if (mInterstitialAd.isLoaded) {
//            mInterstitialAd.show()
//        } else {
//            replay()
//        }
    }

    private var isPendingReplay = false

    private fun replay() {
        if (rewardCount > 0) {
            rewardCount --
        }
        isPendingReplay = true
        fullScreenRequest()
        val handler = Handler()
        handler.postDelayed({ prepareReplay() }, 1000)
    }

    private fun prepareReplay() {
        binding.drawingView.setMode(DrawingView.PREVIEW_MODE)
        val pathData = PathData()
        pathData.setLineColoringModels(binding.drawingView.allPath)
        initReplayPainter(pathData)
        mPainter!!.next()
    }

    private fun initReplayPainter(pathData: PathData) {
        mPainter = object : ReplayDrawingSequence(binding.drawingView) {
            override fun onFinishReadLine() {
                binding.drawingView.clear()
                updateDrawingMode(mMenuPainter.getCurrentPaintType())
                binding.drawingView.invalidate()
                isPendingReplay = false
            }

            override fun onAddPoint(action: Int, x: Float, y: Float) {

            }
        }
        mPainter!!.setMode(ReplayDrawingSequence.MODE_PREVIEW)
        mPainter!!.setPath(pathData)
    }
}
