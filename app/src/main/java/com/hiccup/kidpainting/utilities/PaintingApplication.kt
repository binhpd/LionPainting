package com.hiccup.kidpainting.utilities

import android.app.Application
import android.os.Build
import com.hiccup.kidpainting.utilities.billing.BillingClientLifecycle
import com.hiccup.kidpainting.data.SubRepository
import com.hiccup.kidpainting.data.disk.SubLocalDataSource
import com.hiccup.kidpainting.data.disk.db.AppDatabase
import com.hiccup.kidpainting.data.network.SubRemoteDataSource
import com.hiccup.kidpainting.data.network.firebase.FakeServerFunctions
import com.hiccup.kidpainting.data.network.firebase.ServerFunctions
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.hiccup.kidpainting.R
import com.hiccup.kidpainting.common.RemoteConfigHelper
import com.hiccup.kidpainting.notification.DailyNotificationScheduler
import com.hiccup.kidpainting.notification.NotificationHelper
import com.hiccup.kidpainting.utilities.billing.BillingConstants.USE_FAKE_SERVER
import com.hiccup.kidpainting.utilities.tracking.FireBaseTracker

class PaintingApplication : Application() {

    private val database: AppDatabase
        get() = AppDatabase.getInstance(this)

    private val subLocalDataSource: SubLocalDataSource
        get() = SubLocalDataSource.getInstance(database.subscriptionStatusDao())

    private val serverFunctions: ServerFunctions
        get() {
            return if (USE_FAKE_SERVER) {
                FakeServerFunctions.getInstance()
            } else {
                ServerFunctions.getInstance()
            }
        }

    private val subRemoteDataSource: SubRemoteDataSource
        get() = SubRemoteDataSource.getInstance(serverFunctions)

    val billingClientLifecycle: BillingClientLifecycle
        get() = BillingClientLifecycle.getInstance(this)

    val repository: SubRepository
        get() = SubRepository.getInstance(subLocalDataSource, subRemoteDataSource, billingClientLifecycle)

    override fun onCreate() {
        super.onCreate()
        FireBaseTracker.init(applicationContext)
        FirebaseRemoteConfig.getInstance().setDefaultsAsync(R.xml.remote_config_defaults)
        RemoteConfigHelper.getInstance()
        SoundEffect.instance.init(this)

        settingDailyAlarm()
        var localeManager = LocaleManager(this)

        if (AppHelper.isVietNamLanguage()) {
            localeManager.setNewLocale(this, "en")
        }
//        Fabric.with(this, Crashlytics())

//        val config = RateThisApp.Config(3, 2)
//        RateThisApp.init(config)
    }

    private fun settingDailyAlarm() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationHelper.createChannel(this, NotificationHelper.DAILY_CHANNEL)
        }
        if (!SettingApp.getInstance().isScheduleDailyNotification(this)) {
            DailyNotificationScheduler(this).schedule()
            SettingApp.getInstance().markScheduleDailyNotification(this, true);
        }
    }
}