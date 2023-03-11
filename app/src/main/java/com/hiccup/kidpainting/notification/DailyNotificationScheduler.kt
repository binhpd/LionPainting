package com.hiccup.kidpainting.notification

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import com.hiccup.kidpainting.utilities.AppConstants.EXTRA_OPEN_FROM
import com.hiccup.kidpainting.utilities.tracking.FireBaseEvent
import java.util.*


/**
 * show notification back to app in day 2 and day 7 day 15 from install day
 */
class DailyNotificationScheduler(val context: Context) {
    fun schedule() {
        enableNotificationOnDay(context, 2)
        enableNotificationOnDay(context, 7)
        enableNotificationOnDay(context, 15)
    }

    private fun enableNotificationOnDay(context: Context, day: Int) {
        val intent = Intent(context, DailyReceiver::class.java)
        intent.putExtra(EXTRA_OPEN_FROM, getNotificationTracking(day))
        val pendingInt = PendingIntent.getBroadcast(context, 11, intent, PendingIntent.FLAG_IMMUTABLE)

        val calendar = Calendar.getInstance()
        calendar.add(Calendar.DATE, day)
        calendar.set(Calendar.HOUR_OF_DAY, 20)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)

        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.timeInMillis, pendingInt)
    }

    private fun getNotificationTracking(day: Int): String {
        return when(day) {
            2-> FireBaseEvent.OPEN_APP_FROM_DAILY_NOTIFICATION_DAY2
            7-> FireBaseEvent.OPEN_APP_FROM_DAILY_NOTIFICATION_DAY7
            15-> FireBaseEvent.OPEN_APP_FROM_DAILY_NOTIFICATION_DAY15
            else -> FireBaseEvent.OPEN_APP_FROM_DAILY_NOTIFICATION_DAY2
        }
    }
}

