package com.example.weatherforecast.ui.notifications

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import com.example.weatherforecast.model.Pojos.Constants

class ActionReceiver : BroadcastReceiver() {
    var r=com.example.weatherforecast.ui.notifications.AlarmReciver.r
    @RequiresApi(Build.VERSION_CODES.S)
    override fun onReceive(context: Context, intent: Intent) {
        var notificationHelper=NotificationHelper(context)

            if (intent.action.equals(Constants.ACTION_SNOOZE))
            {
                Log.e("onReceive", "onReceive: pressed ", )
                r.stop()
                notificationHelper.alarmNotificationManager(context).cancel(intent.getIntExtra(Constants.EXTRA_NOTIFICATION_ID,1))

            }
    }
}