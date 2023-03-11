package com.hiccup.kidpainting.activities

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Handler
import android.util.DisplayMetrics
import android.view.View
import android.view.ViewTreeObserver
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import com.hiccup.kidpainting.R
import com.hiccup.kidpainting.databinding.ActivityPractiveColoringBinding
import com.hiccup.kidpainting.databinding.ActivitySelectGraphicBinding
import com.hiccup.kidpainting.drawinghelper.AssetsStateLoader
import com.hiccup.kidpainting.drawinghelper.BasePath
import com.hiccup.kidpainting.drawinghelper.replay.BaseLoader
import com.hiccup.kidpainting.drawinghelper.replay.ReplayDrawingSequence
import com.hiccup.kidpainting.models.PathData
import com.hiccup.kidpainting.pref.AppPreference
import com.hiccup.kidpainting.utilities.LogUtils
import com.hiccup.kidpainting.utilities.SoundEffect
import com.hiccup.kidpainting.utilities.extension.viewBinding
import com.hiccup.kidpainting.utilities.tracking.FireBaseEvent
import com.hiccup.kidpainting.utilities.tracking.FireBaseTracker
import com.hiccup.kidpainting.view.ColoringView

/**
 * Created by ${binhpd} on 3/23/2016.
 */
class PracticeColoringActivity: BasePaintingActivity(), View.OnClickListener {

    private var mPainter: ReplayDrawingSequence? = null

    // Screen mWidth
    private var mDrawingWidth: Int = 0
    private var mDrawingHeight: Int = 0

    private var index = 0

    private var isMersured = false

    override val binding by viewBinding {
        ActivityPractiveColoringBinding.inflate(layoutInflater)
    }

    override fun onInitValue() {
        super.onInitValue()
        initView()
        // init panting panel
        initPanel()
    }

    override fun onResume() {
        super.onResume()
        if (isMersured) {
            resumeDraw()
        }
        playAudio()
    }

    override fun onStop() {
        super.onStop()
        pauseAudio()
    }

    private fun initPanel() {
        binding.mDrawingView!!.viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
            @SuppressLint("NewApi")
            override fun onGlobalLayout() {
                binding.mDrawingView!!.viewTreeObserver.removeOnGlobalLayoutListener(this)
                //now we can retrieve the mWidth and height
                mDrawingWidth = binding.mDrawingView!!.width
                mDrawingHeight = binding.mDrawingView!!.height
                // get screen size
                val manager = windowManager
                val metric = DisplayMetrics()
                manager.defaultDisplay.getMetrics(metric)
                preparePainter(index)
            }
        })

        binding.mDrawingView!!.setMode(ColoringView.PREVIEW_MODE)
    }

    override fun onClick(view: View) {
        when (view.id) {
//            R.id.btnNext -> mPainter!!.next()
//
//            R.id.btnPrevious -> mPainter!!.previous()

//            R.id.btnReplay -> mPainter!!.replay()

            R.id.ivBackPreview -> {
                if (mPainter!!.isRunning) {
                    return
                }
                index = 0
                playGuide()
                toggleIntroduction()
            }

            R.id.ivNextPreview -> {
                if (mPainter!!.isRunning) {
                    return
                }
                index = 1
                playGuide()
                toggleIntroduction()
            }

            R.id.tvReview -> {
                val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(REVIEW_NEW_FEATURE_LINK))
                startActivity(browserIntent)
                FireBaseTracker.sendEvent(FireBaseEvent.INTRODUCTION_CLICK_REVIEW)
            }

            R.id.ivBack -> finish()
        }
        SoundEffect.instance.playSound(R.raw.tap)
    }

    private fun initView() {
        binding.btnNext.setOnClickListener(this)
        binding.btnPrevious.setOnClickListener(this)
        binding.btnReplay.setOnClickListener(this)
        binding.ivBackPreview.setOnClickListener(this)
        binding.ivNextPreview.setOnClickListener(this)
        binding.tvReview.setOnClickListener(this)
        binding.ivBack.setOnClickListener(this)
    }

    private fun playGuide() {
        binding.mDrawingView!!.clear()
        preparePainter(index)
    }

    private fun resumeDraw() {
        mPainter!!.resume()
    }


    private fun toggleIntroduction() {
        val animation = AlphaAnimation(1.0f, 0.0f)
        animation.duration = 200
        animation.repeatCount = 1
        animation.repeatMode = Animation.REVERSE
        animation.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation) {

            }

            override fun onAnimationEnd(animation: Animation) {

            }

            override fun onAnimationRepeat(animation: Animation) {
                binding.tvComingSoon.setText(
                        if (binding.tvComingSoon.text.toString() == getString(R.string.feature_comming_soon))
                            R.string.learn_by_step
                        else
                            R.string.feature_comming_soon)
            }
        })
        binding.tvComingSoon.startAnimation(animation)
    }

    private fun previewWithDelay() {
        val handler = Handler()
        handler.postDelayed({
            isMersured = true
            mPainter?.next()
        }, 1000)
    }

    private fun preparePainter(index: Int) {
        val jsonReader = AssetsStateLoader(this, getPreviewFile(index), mDrawingWidth, mDrawingHeight)
        jsonReader.listener = object : BaseLoader.LoadListener {
            override fun onLoadSuccess(result: List<BasePath>?) {
                if (result != null && result.isNotEmpty()) {
                    val pathData = PathData()
                    pathData.setLineColoringModels(result as List<BasePath>?)
                    initDrawerSequence(pathData)

                    previewWithDelay()
                } else {
                    LogUtils.e("Lỗi tải pathData")
                }
            }
        }
        jsonReader.execute()
    }

    private fun initDrawerSequence(pathData: PathData?) {
        mPainter = object : ReplayDrawingSequence(binding.mDrawingView) {
            override fun onFinishReadLine() {
                if (index == 0) {
                    FireBaseTracker.sendEvent(FireBaseEvent.INTRODUCTION_FINISH_1)
                    binding.ivNextPreview.visibility = View.VISIBLE
                    binding.ivBackPreview.visibility = View.GONE
                } else {
                    FireBaseTracker.sendEvent(FireBaseEvent.INTRODUCTION_FINISH_2)
                    binding.ivNextPreview.visibility = View.GONE
                    binding.ivBackPreview.visibility = View.VISIBLE
                    if (!AppPreference.getInstance().isShowReview(this@PracticeColoringActivity)) {
                        FireBaseTracker.sendEvent(FireBaseEvent.INTRODUCTION_SHOW_REVIEW)
                        AppPreference.getInstance().setShowReview(this@PracticeColoringActivity, true)
                    }
                }
            }

            override fun onAddPoint(action: Int, x: Float, y: Float) {

            }
        }
        mPainter!!.setMode(ReplayDrawingSequence.MODE_PREVIEW)
        mPainter!!.setPath(pathData)
    }

    private fun getPreviewFile(index: Int): String {
        when (index) {
            0 -> return "training/1-scream.txt"
            1 -> return "training/3-cream.txt"
        }
        return "training/1-scream.txt"
    }

    override fun onPause() {
        super.onPause()
        if (mPainter != null) {
            mPainter!!.stop()
        }
    }

    override fun getAudioSrc(): Int {
        return R.raw.intro
    }

    companion object {
        private val TAG = PracticeColoringActivity::class.java.simpleName
        private val MILISECONDS_PER_SECOND = 1000
        val DRAWINGS_REQUEST_CODE = 999
        val REVIEW_NEW_FEATURE_LINK = "https://goo.gl/forms/zS6PjiYVR96hHcTr1"
    }
}
