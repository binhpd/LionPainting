package com.hiccup.kidpainting.activities

import android.content.Intent
import android.os.Handler
import android.view.Gravity
import android.view.View
import com.hiccup.kidpainting.R
import com.hiccup.kidpainting.adapters.OnSelectListener
import com.hiccup.kidpainting.adapters.PhotoCollectionAdapter
import com.hiccup.kidpainting.databinding.ActivitySelectGraphicBinding
import com.hiccup.kidpainting.models.CollectionItem
import com.hiccup.kidpainting.models.PhotoModel
import com.hiccup.kidpainting.pref.AppPurchase
import com.hiccup.kidpainting.utilities.*
import com.hiccup.kidpainting.utilities.extension.viewBinding
import com.hiccup.kidpainting.utilities.tracking.FireBaseEvent
import com.hiccup.kidpainting.utilities.tracking.FireBaseTracker
import com.hiccup.kidpainting.view.dialog.FreeUserDialog

class SelectGraphicActivity : BasePaintingActivity() {

    override val binding by viewBinding {
        ActivitySelectGraphicBinding.inflate(layoutInflater)
    }

    companion object {
        @JvmStatic
        val COLLECTION_TYPE = "COLLECTION_TYPE"
        val MODE_OPEN = "MODE_OPEN"
        val EXTRA_OPEN_FROM_SHARE = "EXTRA_OPEN_FROM_SHARE"
        val MODE_ONE_SHOT = 1
        val MODE_NORMAL = 0
        const val OPEN_DRAWING_ACTIVITY = 111
    }

//    private lateinit var mInterstitialAd: InterstitialAd
    private lateinit var collectionItem: CollectionItem
    private var index: Int = 0
    private var mOpenFromOnboarding: Boolean = false
    private var mOpenMode = 0
    private var mIsUnlockFromShare = false

    override fun onInitValue() {
        super.onInitValue()
        binding.ivBack.setOnClickListener {
            SoundEffect.instance.playSound(R.raw.tap)
            finish()
        }
        mOpenMode = intent.extras?.getInt(MODE_OPEN, 0) ?: 0
        initPhotoList()
        initInterstitialAds()
        checkAndRunOnboarding()
        FireBaseTracker.sendEvent(FireBaseEvent.GRAPHIC_PHOTO_OPEN)
//        val reward = RewardHelper.insBillingClientLifecycle.kt
//        BillingUtilities.instance.rewardAccessCollection
//        if (reward >= 0) {
//            RewardHelper.instance.setRewardCollection(reward-1)
//        }
    }

    override fun onResume() {
        super.onResume()
        loadAds()
    }

//    override fun onRewarded(reward: RewardItem) {
//        super.onRewarded(reward)
//        RewardHelper.getInstance().setRewardCollection(reward.amount)
//        initPhotoList()
//    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        loadAds()
    }

    private fun initPhotoList() {
        val collectionItem = intent.extras?.get(COLLECTION_TYPE) as CollectionItem
        val photos = getPhotos(collectionItem.photos)

        val adapter = PhotoCollectionAdapter(this, collectionItem.name, photos)
        adapter.listener = OnSelectListener { _, index ->
            handleSelectItem(collectionItem, index)
        }
        binding.rvPhotos.adapter = adapter
        val spancount = if (UIUtils.isTablet(this)) 3 else 2
        binding.rvPhotos.layoutManager = androidx.recyclerview.widget.GridLayoutManager(this, spancount, androidx.recyclerview.widget.LinearLayoutManager.HORIZONTAL, false)
    }

    private fun handleSelectItem(collectionItem: CollectionItem, index: Int) {
        this.collectionItem = collectionItem
        this.index = index
        SoundEffect.instance.playSound(R.raw.tap)

        if (AppPurchase.instance.isHasPurchase(this)) {
            openDrawingScreen(collectionItem, index)
            return
        }

        if (collectionItem.photos[index].isEnable) {
            showInterstitial(collectionItem, index)
            FireBaseTracker.sendEvent(FireBaseEvent.GRAPHIC_PHOTO_SELECT_ITEM)
        } else {
            showFreeDialog()
        }
    }

    private fun showFreeDialog() {
        val dialog = FreeUserDialog(this, object : FreeUserDialog.DialogListener {
            override fun onClickShare() {
                FireBaseTracker.sendEvent(FireBaseEvent.SHOW_REWARD_VIDEO)
                loadAds()
            }

            override fun onClickGotoStore() {
                FireBaseTracker.sendEvent(FireBaseEvent.CLICK_OPEN_STORE_REPLAY_DIALOG)
                openStore()
            }

            override fun onDismiss() {
                fullScreenRequest()
            }

        })

        dialog.showDilog(getString(R.string.share_facebook_replay), getString(R.string.purchase_now_to_unlock_feature))
    }

    private fun openStore() {
        val intent = Intent(this, LockKidActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_CLEAR_TOP
        startActivity(intent)
    }


    private fun getPhotos(photos: ArrayList<PhotoModel>): ArrayList<PhotoModel> {
        if (AppPurchase.instance.isHasPurchase(this) || mIsUnlockFromShare || RewardHelper.instance.rewardAccessCollection > 0) {
            for (item in photos) {
                item.isEnable = true
            }
        } else {
            for (i in 0..8) {
                photos[i].isEnable = true
            }
        }
        return photos
    }

    private fun openDrawingScreen(collectionItem: CollectionItem, index: Int) {
        SoundEffect.instance.playSound(R.raw.tap)
        val intent = Intent(this@SelectGraphicActivity, DrawingActivity::class.java)
        intent.putExtra(DrawingActivity.EXTRA_PHOTO, collectionItem.photos[index])
        intent.putExtra(DrawingActivity.EXTRA_COLLECTION_NAME, collectionItem.name)

        startActivityForResult(intent, OPEN_DRAWING_ACTIVITY)
    }

    override fun getAudioSrc(): Int {
        return R.raw.drawing_audio
    }

    private fun initInterstitialAds() {
//        mInterstitialAd = InterstitialAd(this).apply {
//            adUnitId = getString(R.string.ads_unit_id_interstitial)
//            adListener = (object : AdListener() {
//                override fun onAdClosed() {
//                    openDrawingScreen(collectionItem, index)
//                }
//            })
//        }
    }

    private fun loadAds() {
//        if (!mInterstitialAd.isLoading && !mInterstitialAd.isLoaded) {
//            var adRequest: AdRequest = if (BuildConfig.DEBUG) {
//                AdRequest.Builder()
//                        .addTestDevice(AppConstants.TEST_DEVICE)
//                        .build()
//            } else {
//                AdRequest.Builder()
//                        .build()
//            }
//            mInterstitialAd.loadAd(adRequest)
//        }
    }

    private fun showInterstitial(collectionItem: CollectionItem, index: Int) {
        // don't show ads on showing onboarding
        if (mOpenFromOnboarding) {
            openDrawingScreen(collectionItem, index)
            return
        }

//        if (mInterstitialAd.isLoaded) {
//            mInterstitialAd.show()
//        } else {
//            openDrawingScreen(collectionItem, index)
//        }
    }

    private fun checkAndRunOnboarding() {
        val handler = Handler()
        if (!StorageUtils.getBooleanFromSharedPref(this, SelectGraphicActivity::class.java.simpleName, false)) {
            handler.postDelayed({ showToolTip(binding.rvPhotos.getChildAt(0)) }, 1000)
            mOpenFromOnboarding = true
        }
    }

    private fun showToolTip(view: View) {
        OnboardingHelper.showBoarding(this, getString(R.string.onboarding_step3),
                Gravity.RIGHT or Gravity.CENTER_VERTICAL,
                Gravity.TOP, view,
                SelectGraphicActivity::class.java.simpleName)
    }
}