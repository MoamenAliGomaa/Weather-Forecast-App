package com.example.weatherforecast.ui.notifications



import android.app.Notification
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.media.MediaPlayer
import android.media.Ringtone
import android.media.RingtoneManager
import android.net.Uri
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.annotation.Nullable
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModelProvider
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.work.WorkManager

import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.example.kotlinproducts.view.API
import com.example.weatherforecast.R
import com.example.weatherforecast.model.Network.RemoteDataSource
import com.example.weatherforecast.model.Pojos.Alert
import com.example.weatherforecast.model.Pojos.Constants
import com.example.weatherforecast.model.Repository
import com.example.weatherforecast.model.SharedPrefrences.SharedManger
import com.example.weatherforecast.model.Utils
import com.example.weatherforecast.model.database.LocalDataSource
import com.example.weatherforecast.model.database.WeatherDataBse
import com.google.gson.Gson
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collectLatest
import kotlin.math.absoluteValue


class AlarmReciver : BroadcastReceiver() {
    lateinit var notificationManager:NotificationManager
    var notificationId:Int?=null
    companion object{
        lateinit var notification:Uri
        lateinit var  r:Ringtone

    }
    @RequiresApi(Build.VERSION_CODES.M)
    override fun onReceive(context: Context, intent: Intent) {
        SharedManger.init(context)
        var repo= Repository.getInstance(LocalDataSource(context), RemoteDataSource())
        var alertSettings=repo.getAlertSettings()
        var alertJson = intent.getStringExtra(Constants.Alert)
        var alert = Gson().fromJson(alertJson, Alert::class.java)
        val notificationHelper = NotificationHelper(context)
        notificationId=1

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            notificationManager=  notificationHelper.alarmNotificationManager(context)
        }




            Log.e("onReceive", "ladskjflsakjdflskjdflskjdfslkjdflasdf")
            Toast.makeText(context, "OnReceive alarm test", Toast.LENGTH_SHORT).show()
            CoroutineScope(Dispatchers.IO).launch {
                if (!Utils.isDaily(alert.startTime,alert.endTime))
                {
                    Utils.canelAlarm(context,alert.toString(),alert.startTime.toInt())
                    repo.deleteAlert(alert)
                    WorkManager.getInstance(context.applicationContext).cancelAllWorkByTag(alert.startTime.toString())
                }
                try {
                    repo.getCurrentWeather(lat = alert.lat.toString(), lon = alert.lon.toString())
                        .collectLatest {
                            val bitmap = arrayOf<Bitmap?>(null)

                                Glide.with(context)
                                    .asBitmap()
                                    .load(Utils.getIconUrl(it.current.weather[0].icon))
                                    .into(object : CustomTarget<Bitmap?>() {
                                        @RequiresApi(Build.VERSION_CODES.S)
                                        override fun onResourceReady(
                                            resource: Bitmap,
                                            @Nullable transition: Transition<in Bitmap?>?
                                        ) {
                                            bitmap[0] = resource

                                            Log.e("onReceive", "onResourceReady: "+resource )
                                            notification = Uri.parse(("android.resource://" + context.applicationContext.packageName) + "/" + R.raw.weather_alarm)
                                            r = RingtoneManager.getRingtone(
                                                context.applicationContext,
                                                notification
                                            )
                                            if(alertSettings?.isALarm==true && !alertSettings.isNotification){
                                                r.play()
                                                notificationManager.notify(notificationId!!, notificationHelper.getNotification(context,
                                                    notificationId!!, Utils.getAddressEnglish(context,alert.lat,alert.lon)!!, it.current.weather[0].description,bitmap[0]!!))

                                            }
                                            if(alertSettings?.isALarm==false && alertSettings.isNotification){
                                             notificationManager.notify(notificationId!!,notificationHelper.getNotificationBuilder(
                                                    Utils.getAddressEnglish(context,alert.lat,alert.lon)!!,  it.current.weather[0].description,context,
                                                    bitmap[0]!!

                                                ).build())
                                            }

                                        }

                                        override fun onLoadCleared(@Nullable placeholder: Drawable?) {

                                        }
                                    })




                        }


                } finally {
                    cancel()
                }

            }






    }


}

