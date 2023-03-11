package com.hiccup.kidpainting.activities

import android.app.Activity
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.viewbinding.ViewBinding
import androidx.viewbinding.ViewBindings
import com.hiccup.kidpainting.utilities.AppHelper.getAppLanguage


/**
 * Created by hiccup on 26/08/2015.
 */
abstract class BaseActivity: AppCompatActivity() {

//    protected var adView: AdView? = null
//    protected lateinit var mRewardedVideoAd: RewardedVideoAd
    /**
     * get content view of activity
     *
     * @return
     */
//    protected abstract val contentView: Int
    protected abstract val binding: ViewBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fullScreenRequest()
        setContentView(binding.root)
        onInitValue()
        loadLanguageResource(getAppLanguage())
//        MobileAds.initialize(this, getString(R.string.ads_app_id))
//        this.adView = findViewById(R.id.adView)
        initRewardVideo()
        loadAds()
    }

    override fun onResume() {
        super.onResume()
//        mRewardedVideoAd.resume(this)
        fullScreenRequest()
    }

    override fun onPause() {
        super.onPause()
//        adView?.pause()
//        mRewardedVideoAd.pause(this)

    }

    /** Called before the activity is destroyed  */
    override fun onDestroy() {
        super.onDestroy()
//        adView?.destroy()
//        mRewardedVideoAd.destroy(this)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
    }

    protected fun fullScreenRequest() {
        if (Build.VERSION.SDK_INT in 12..18) { // lower api
            val v = this.window.decorView
            v.systemUiVisibility = View.GONE
        } else if (Build.VERSION.SDK_INT >= 19) {
            //for new api versions.
            val decorView = window.decorView
            val uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
            decorView.systemUiVisibility = uiOptions
        }
    }

    protected abstract fun onInitValue()

    protected open fun loadLanguageResource(appLanguage: String) {}

    protected fun loadRewardedVideoAd() {
//        if (BuildConfig.DEBUG) {
//            mRewardedVideoAd.loadAd(getString(R.string.ads_unit_id_reward),
//                    AdRequest.Builder().addTestDevice(AppConstants.TEST_DEVICE).build())
//        } else {
//            mRewardedVideoAd.loadAd(getString(R.string.ads_unit_id_reward),
//                    AdRequest.Builder().build())
//            Toast.makeText(this, "Start loading", Toast.LENGTH_SHORT).show()
//        }

    }

    private fun loadAds() {
//        if (adView == null) {
//            return
//        }
//
//        if (AppPurchase.isRemoveAds(this) || AppPurchase.isFullPurchase(this)) {
//            adView!!.visibility = View.GONE
//        } else {
//            adView!!.visibility = View.VISIBLE
//            val adRequest = AdRequest.Builder().build()
//            adView!!.adListener = object: AdListener() {
//                override fun onAdLoaded() {
//                    super.onAdLoaded()
//                    adView!!.visibility = View.VISIBLE
//                }
//            }
//            adView!!.loadAd(adRequest)
//        }
//        adView?.resume()
    }

    private fun initRewardVideo() {
//        mRewardedVideoAd = MobileAds.getRewardedVideoAdInstance(this)
//        mRewardedVideoAd.rewardedVideoAdListener = this
    }

//    override fun onRewarded() {
//        Toast.makeText(this, "onRewarded", Toast.LENGTH_SHORT).show()
//    }
//
//    override fun onRewardedVideoAdLeftApplication() {
//        Toast.makeText(this, "onRewardedVideoAdLeftApplication", Toast.LENGTH_SHORT).show()
//    }
//
//    override fun onRewardedVideoAdClosed() {
//        Toast.makeText(this, "onRewardedVideoAdClosed", Toast.LENGTH_SHORT).show()
//    }
//
//    override fun onRewardedVideoAdFailedToLoad(errorCode: Int) {
//        Toast.makeText(this, "onRewardedVideoAdFailedToLoad $errorCode", Toast.LENGTH_SHORT).show()
//    }
//
//    override fun onRewardedVideoAdLoaded() {
//        Toast.makeText(this, "onRewardedVideoAdLoaded", Toast.LENGTH_SHORT).show()
//        mRewardedVideoAd.show()
//    }

//    override fun onRewardedVideoAdOpened() {
//        Toast.makeText(this, "onRewardedVideoAdOpened", Toast.LENGTH_SHORT).show()
//    }
//
//    override fun onRewardedVideoStarted() {
//        Toast.makeText(this, "onRewardedVideoStarted", Toast.LENGTH_SHORT).show()
//    }
//
//    override fun onRewardedVideoCompleted() {
//        Toast.makeText(this, "onRewardedVideoCompleted", Toast.LENGTH_SHORT).show()
//    }

    companion object {
        var Broadcast_PLAY_AUDIO = "com.hiccup.kidpainting.activities.BaseActivity.PlayAudio"
    }

}
