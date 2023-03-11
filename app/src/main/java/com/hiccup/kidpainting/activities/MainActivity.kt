package com.hiccup.kidpainting.activities

//import com.kobakei.ratethisapp.RateThisApp
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import android.util.Log
import android.view.Gravity
import android.view.View
import androidx.core.app.ActivityOptionsCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.hiccup.kidpainting.R
import com.hiccup.kidpainting.activities.store.BillingViewModel
import com.hiccup.kidpainting.activities.store.StoreActivity
import com.hiccup.kidpainting.databinding.ActivityMainBinding
import com.hiccup.kidpainting.pref.AppPurchase
import com.hiccup.kidpainting.services.MediaService
import com.hiccup.kidpainting.utilities.*
import com.hiccup.kidpainting.utilities.billing.BillingClientLifecycle
import com.hiccup.kidpainting.utilities.extension.viewBinding
import com.hiccup.kidpainting.utilities.tracking.FireBaseEvent
import com.hiccup.kidpainting.utilities.tracking.FireBaseTracker
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import tourguide.tourguide.TourGuide

class MainActivity : BasePaintingActivity(), View.OnClickListener {

    private var playIntent: Intent? = null
    private val mTourGuide: TourGuide? = null
    //-----------------------------------------

    private var mMusicService: MediaService? = null

    private var mAudioServiceBound: Boolean = false

    private lateinit var billingClientLifecycle: BillingClientLifecycle

    override val binding by viewBinding {
        ActivityMainBinding.inflate(layoutInflater)
    }

    private val REVIEW_APP_LINK = "https://goo.gl/forms/Dk34EFBTgqmcN1qk1"

    private val serviceConnection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName, service: IBinder) {
            val binder = service as MediaService.AudioBinder
            //get service
            mMusicService = binder.service
            //pass list
            mAudioServiceBound = true
            Log.d(TAG, "Service Bound")
        }

        override fun onServiceDisconnected(name: ComponentName) {
            mAudioServiceBound = false
            Log.d(TAG, "Service unBound")
        }
    }

    override fun onInitValue() {
        super.onInitValue()

        FirebaseRemoteConfig.getInstance().fetch(100).addOnCompleteListener {
            if (it.isSuccessful) {
                FirebaseRemoteConfig.getInstance().fetchAndActivate()
            }
        }

        billingClientLifecycle = (application as PaintingApplication).billingClientLifecycle
        lifecycle.addObserver(billingClientLifecycle)
        GlobalScope.launch {
            ((application as PaintingApplication).billingClientLifecycle.purchases).collect { purchases ->
                if (purchases.isNotEmpty()) {
                    AppPurchase.instance.saveStatePurchase(baseContext, purchases[0].purchaseToken)
                } else {
                    AppPurchase.instance.clear(baseContext)
                }
            }
        }
        AppPurchase.instance.purchaseChange.observe(this, Observer {
            checkPurchaseState()
        })

        // show rate app
        showRateApp()

        // tracking source opened app
        trackingSourceOpenedApp()
        initView()

        binding.ivLanguage.visibility = View.GONE
        //Todo:
//        checkNewFeature(groupLearning)

        Handler(Looper.getMainLooper()).postDelayed({
            showToolTip()
        }, 1500)

        // redirect to target screen
        Handler(Looper.getMainLooper()).postDelayed({
            RedirectHelper.redirect(this, intent)
        }, 500)

    }

    private fun showRateApp() {
//        RateThisApp.onCreate(this);
//        // If the condition is satisfied, "Rate this app" dialog will be shown
//        RateThisApp.showRateDialogIfNeeded(this)
//
//        RateThisApp.setCallback(object : RateThisApp.Callback {
//            override fun onYesClicked() {
//                FireBaseTracker.sendEvent(FireBaseEvent.RATE_CLICK_YES)
//            }
//
//            override fun onNoClicked() {
//                FireBaseTracker.sendEvent(FireBaseEvent.RATE_CLICK_NO_EVENT)
//            }
//
//            override fun onCancelClicked() {
//                FireBaseTracker.sendEvent(FireBaseEvent.RATE_CLICK_CANCEL)
//            }
//        })
    }

    override fun loadLanguageResource(appLanguage: String) {
        if (AppHelper.isVietNamLanguage()) {
            Glide.with(this).load(R.drawable.ic_title).into(binding.ivTitle)
            Glide.with(this).load(R.drawable.ic_painting_name).into(binding.ivNameColoring)
            Glide.with(this).load(R.drawable.ic_gallery_name).into(binding.ivGalleryName!!)
            Glide.with(this).load(R.drawable.ic_learning_name).into(binding.ivLearningName!!)
            Glide.with(this).load(R.drawable.ic_freestyle_name).into(binding.ivFreeStyleName!!)
        } else {
            Glide.with(this).load(R.drawable.ic_title_en).into(binding.ivTitle)
            Glide.with(this).load(R.drawable.ic_painting_name_en).into(binding.ivNameColoring)
            Glide.with(this).load(R.drawable.ic_gallery_name_en).into(binding.ivGalleryName!!)
            Glide.with(this).load(R.drawable.ic_learning_name_en).into(binding.ivLearningName!!)
            Glide.with(this).load(R.drawable.ic_freestyle_name_en).into(binding.ivFreeStyleName!!)
        }
    }

    private fun trackingSourceOpenedApp() {
        if (intent.extras != null && intent.extras!!.containsKey(AppConstants.EXTRA_OPEN_FROM)) {
            FireBaseTracker.sendEvent(intent.extras!!.getString(AppConstants.EXTRA_OPEN_FROM))
        }
    }

    private fun initView() {
        binding.ivPainting.setOnClickListener(this)
        binding.ivGallery.setOnClickListener(this)
        binding.groupColoring.setOnClickListener(this)
        binding.groupLearning.setOnClickListener(this)
        binding.ivMute.setOnClickListener(this)
        binding.ivStore.setOnClickListener(this)
        binding.tvReview.setOnClickListener(this)
        binding.groupShare.setOnClickListener(this)
    }

    private fun checkPurchaseState() {
        if (AppPurchase.instance.isHasPurchase(this)) {
            binding.tvUnlock?.setText(R.string.you_are_premium)
            binding.ivSale?.visibility = View.GONE
        } else {
            binding.tvUnlock?.setText(R.string.unlock)
            binding.ivSale?.visibility = View.VISIBLE
        }
    }


    override fun onStart() {
        if (playIntent == null && !mAudioServiceBound) {
            playIntent = Intent(this, MediaService::class.java)

            bindService(playIntent, serviceConnection, Context.BIND_AUTO_CREATE)
            startService(playIntent)
        }
        super.onStart()

    }

    public override fun onResume() {
        super.onResume()
        playBackgroundAudio()
        checkPurchaseState()
    }


    public override fun onPause() {
        super.onPause()
        pauseAudio()
    }

    override fun onStop() {
        if (mAudioServiceBound) {
            pauseAudio()
        }

        super.onStop()
    }

    override fun onDestroy() {
        if (mAudioServiceBound) {
            unbindService(serviceConnection)
            //service is active
            if (mMusicService != null) {
                mMusicService!!.stopSelf()
            }
            mMusicService = null
        }
        super.onDestroy()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putBoolean(SERVICE_STATE, mAudioServiceBound)
        super.onSaveInstanceState(outState)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        mAudioServiceBound = savedInstanceState.getBoolean(SERVICE_STATE)
        super.onRestoreInstanceState(savedInstanceState)
    }


    override fun onClick(view: View) {
        when (view.id) {
            R.id.groupColoring -> {
                mTourGuide?.cleanUp()
                val intent = Intent(this@MainActivity, CollectionActivity::class.java)
                startActivity(intent)
                FireBaseTracker.sendEvent(FireBaseEvent.HOME_CLICK_COLLECTION)
            }

            R.id.ivGallery -> {
                intent = Intent(this@MainActivity, GalleryActivity::class.java)
                startActivity(intent)
                FireBaseTracker.sendEvent(FireBaseEvent.HOME_CLICK_GALLERY)
            }

            R.id.groupLearning -> {
                intent = Intent(this@MainActivity, PracticeColoringActivity::class.java)
                val optionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(this@MainActivity,
                    binding.tvComingSoon!!, "new_feature")
                startActivity(intent, optionsCompat.toBundle())
                FireBaseTracker.sendEvent(FireBaseEvent.HOME_CLICK_LEARNING)
            }

            R.id.ivPainting -> {
                intent = Intent(this@MainActivity, DrawingActivity::class.java)
                startActivity(intent)
                FireBaseTracker.sendEvent(FireBaseEvent.HOME_CLICK_CREATIVE)
            }

            R.id.ivMute -> clickToMute()

            R.id.ivStore -> {
                intent = Intent(this@MainActivity, LockKidActivity::class.java)
                startActivity(intent)
                FireBaseTracker.sendEvent(FireBaseEvent.HOME_CLICK_STORE)
            }

            R.id.tvReview -> {
                val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(REVIEW_APP_LINK))
                startActivity(browserIntent)
                FireBaseTracker.sendEvent(FireBaseEvent.HOME_CLICK_APP_REVIEW)
            }

            R.id.groupShare -> {
                FireBaseTracker.sendEvent(FireBaseEvent.SHARE_FACEBOOK_HOME_CLICK)
                shareFacebook()
            }
        }
        SoundEffect.instance.playSound(R.raw.tap)
    }

    private fun shareFacebook() {
        FireBaseTracker.sendEvent(FireBaseEvent.HOME_CLICK_SHARE_FACEBOOK)
    }

    override fun getAudioSrc(): Int {
        return R.raw.main_audio
    }

    // end public region
    //==============================================================================================
    // private region

    private fun clickToMute() {
        if (!SettingApp.MUTE) {
            pauseAudio()
            binding.ivMute.setImageResource(R.drawable.ic_unmute)
            FireBaseTracker.sendEvent(FireBaseEvent.HOME_CLICK_MUTE)
        } else {
            resumeAudio()
            binding.ivMute.setImageResource(R.drawable.ic_mute)
            FireBaseTracker.sendEvent(FireBaseEvent.HOME_CLICK_UNMUTE)
        }
        SettingApp.MUTE = !SettingApp.MUTE
    }

    private fun playBackgroundAudio() {
        if (!mAudioServiceBound) {
            val playerIntent = Intent(this, MediaService::class.java)
            playerIntent.putExtra(MediaService.EXTRA_AUDIO, getAudioSrc())
            startService(playerIntent)
            bindService(playerIntent, serviceConnection, Context.BIND_AUTO_CREATE)
        } else {
            //Service is active
            //Send media with BroadcastReceiver
            val broadcastIntent = Intent(BaseActivity.Broadcast_PLAY_AUDIO)
            broadcastIntent.putExtra(MediaService.EXTRA_AUDIO, getAudioSrc())
            broadcastIntent.putExtra(MediaService.ACTION, MediaService.ACTION_PLAY)
            sendBroadcast(broadcastIntent)
        }
    }

    private fun showToolTip() {
        OnboardingHelper.showBoarding(this, getString(R.string.onboarding_step1),
            Gravity.CENTER,
            Gravity.BOTTOM or Gravity.RIGHT,
            binding.groupColoring,
            MainActivity::class.java.simpleName)
    }

    private fun checkNewFeature(view: View) {
//        if (RemoteConfigHelper.getInstance().getStateNewFeature(this) == RemoteConfigHelper.FEATURE_NEED_UPDATE) {
//            tvComingSoon.text = getString(R.string.feature_release)
//            val animator = ObjectAnimator.ofFloat(view, "translationY", 0f, 45f, 0f)
//            animator.interpolator = EasingInterpolator(Ease.ELASTIC_IN_OUT)
//            animator.startDelay = 1000
//            animator.duration = 1500
//            animator.repeatCount = 20
//            animator.start()
//        }
    }

    private

    companion object {
        private val TAG = MainActivity::class.java.simpleName
        private const val SERVICE_STATE = "SERVICE_STATE"
    }
}