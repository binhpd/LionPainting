package com.hiccup.kidpainting.notification

import android.app.Notification
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import com.hiccup.kidpainting.R
import com.hiccup.kidpainting.activities.MainActivity
import com.hiccup.kidpainting.utilities.AppConstants
import com.hiccup.kidpainting.utilities.LogUtils

class DailyReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        LogUtils.e("Create daily noti: " + intent.getStringExtra(AppConstants.EXTRA_OPEN_FROM))
        val sendIntent = Intent(context, MainActivity::class.java)
        sendIntent.putExtra(AppConstants.EXTRA_OPEN_FROM, intent.getStringExtra(AppConstants.EXTRA_OPEN_FROM))

        val pendingIntent = PendingIntent.getActivity(context, 111, intent, PendingIntent.FLAG_UPDATE_CURRENT)
        val builder = createBuilder(context, NotificationHelper.DAILY_CHANNEL)
        builder.setContentTitle(context.getString(R.string.app_name))
        builder.setContentText(context.getString(R.string.back_to_app))
        builder.setSmallIcon(R.mipmap.ic_launcher)
        builder.setAutoCancel(true)
        builder.setContentIntent(pendingIntent)
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(11, builder.build())
    }

    private fun createBuilder(context: Context, channel: String): Notification.Builder {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            Notification.Builder(context, channel)
        } else {
            Notification.Builder(context)
        }
    }
}
