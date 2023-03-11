package com.example.weatherforecast.ui.notifications

import android.app.AlarmManager
import android.app.PendingIntent
import android.app.PendingIntent.FLAG_IMMUTABLE
import android.content.Context
import android.content.Context.ALARM_SERVICE
import android.content.Intent
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat.getSystemService
import androidx.work.CoroutineWorker
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.example.weatherforecast.model.Pojos.Alert
import com.example.weatherforecast.model.Pojos.Constants
import com.example.weatherforecast.model.Repository
import com.example.weatherforecast.model.Utils
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.withContext
import kotlin.coroutines.coroutineContext


private const val TAG = "MyWorker"
class MyWorker(appContext: Context, workerParams: WorkerParameters) :
  CoroutineWorker(appContext, workerParams) {
    var repository = Repository.getInstance(ctx = appContext )

    override  suspend fun doWork(): Result {
        val alertJson = inputData.getString(Constants.Alert)
        val alert = Gson().fromJson(alertJson, Alert::class.java)
        setAlarm(alert.startTime,alertJson,Utils.generateRandomNumber())
        Log.i(TAG, "doWork: "+alert.toString())
       withContext(Dispatchers.Main){
           Toast.makeText(applicationContext, alert.toString(), Toast.LENGTH_SHORT).show()
       }
        return Result.success()

    }
    @RequiresApi(Build.VERSION_CODES.M)
    private fun setAlarm(dateInMillis: Long,alert:String?,requestCode:Int) {
         var alarmMgr: AlarmManager? = null
         lateinit var alarmIntent: PendingIntent

        alarmMgr = applicationContext.getSystemService(ALARM_SERVICE) as AlarmManager
        alarmIntent = Intent(applicationContext, AlarmReciver::class.java).putExtra(Constants.Alert,alert).let { intent ->
            PendingIntent.getBroadcast(applicationContext, requestCode, intent, FLAG_IMMUTABLE)
        }
        alarmMgr?.setExactAndAllowWhileIdle(
            AlarmManager.RTC_WAKEUP,
            dateInMillis,
            alarmIntent
        )
    }


}
