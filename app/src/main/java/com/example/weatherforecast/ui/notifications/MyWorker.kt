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
import androidx.work.CoroutineWorker
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import com.example.kotlinproducts.view.API
import com.example.weatherforecast.model.Network.RemoteDataSource
import com.example.weatherforecast.model.Pojos.Alert
import com.example.weatherforecast.model.Pojos.Constants
import com.example.weatherforecast.model.Repository
import com.example.weatherforecast.model.SharedPrefrences.SharedManger
import com.example.weatherforecast.model.Utils
import com.example.weatherforecast.model.database.LocalDataSource
import com.example.weatherforecast.model.database.WeatherDataBse
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.Calendar
import kotlin.math.log


private const val TAG = "MyWorker"
class MyWorker(appContext: Context, workerParams: WorkerParameters) :
  CoroutineWorker(appContext, workerParams) {



    override  suspend fun doWork(): Result {
        SharedManger.init(applicationContext)
        var repository= Repository.getInstance(LocalDataSource(applicationContext), RemoteDataSource())
        val alertJson = inputData.getString(Constants.Alert)
        var alert = Gson().fromJson(alertJson, Alert::class.java)
        if(alert.endTime in alert.startTime ..alert.endTime)
        {

                setAlarm(alert.startTime,alertJson,alert.startTime.toInt())
                withContext(Dispatchers.Main){
                    Toast.makeText(applicationContext, "daily worker", Toast.LENGTH_SHORT).show()
                }

        }

        if(alert.endTime<System.currentTimeMillis())
        {
            WorkManager.getInstance(applicationContext).cancelAllWorkByTag(alert.startTime.toString())
            repository.deleteAlert(alert)
            Utils.canelAlarm(applicationContext, alert.toString(),alert.startTime.toInt())
            withContext(Dispatchers.Main) {
                Toast.makeText(applicationContext, "your worker ended", Toast.LENGTH_SHORT).show()
            }
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
