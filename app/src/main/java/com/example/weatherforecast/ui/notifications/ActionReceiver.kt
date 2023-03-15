package com.example.weatherforecast.ui.notifications

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.work.WorkManager
import com.example.weatherforecast.model.Pojos.Alert
import com.example.weatherforecast.model.Pojos.Constants
import com.example.weatherforecast.model.Repository
import com.example.weatherforecast.model.Utils
import com.google.gson.Gson
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collectLatest

class ActionReceiver : BroadcastReceiver() {
    var r=com.example.weatherforecast.ui.notifications.AlarmReciver.r
    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onReceive(context: Context, intent: Intent) {
        var notificationHelper=NotificationHelper(context)
                if (intent.action.equals(Constants.ACTION_SNOOZE)) {
                        r.stop()
                        notificationHelper.alarmNotificationManager(context).cancel(1)
        }
    }
}