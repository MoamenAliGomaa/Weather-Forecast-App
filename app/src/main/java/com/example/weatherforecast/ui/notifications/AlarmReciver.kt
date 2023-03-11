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
import androidx.localbroadcastmanager.content.LocalBroadcastManager

import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.example.weatherforecast.R
import com.example.weatherforecast.model.Pojos.Alert
import com.example.weatherforecast.model.Pojos.Constants
import com.example.weatherforecast.model.Repository
import com.example.weatherforecast.model.Utils
import com.google.gson.Gson
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collectLatest


class AlarmReciver : BroadcastReceiver() {
    lateinit var notificationManager:NotificationManager
    var notificationId:Int?=null
    companion object{
        lateinit var notification:Uri
        lateinit var  r:Ringtone

    }
    @RequiresApi(Build.VERSION_CODES.M)
    override fun onReceive(context: Context, intent: Intent) {


        var alertJson = intent.getStringExtra(Constants.Alert)
        var alert = Gson().fromJson(alertJson, Alert::class.java)
        var repository = Repository.getInstance(ctx = context)
        val notificationHelper = NotificationHelper(context)
        notificationId=1
        Log.e("onReceive", "onReceive: generated id "+notificationId )

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            notificationManager=  notificationHelper.alarmNotificationManager(context)
        }



            Log.e("onReceive", "ladskjflsakjdflskjdflskjdfslkjdflasdf")
            Toast.makeText(context, "OnReceive alarm test", Toast.LENGTH_SHORT).show()
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    repository.getCurrentWeather(lat = alert.lat.toString(), lon = alert.lon.toString())
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
                                        r.play()
                                        notificationManager.notify(notificationId!!, notificationHelper.getNotification(context,
                                            notificationId!!, Utils.getAddressEnglish(context,alert.lat,alert.lon)!!, it.current.weather[0].description,bitmap[0]!!))


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


//fun ringToneManager(context: Context): MediaPlayer? {
//    val alarmSound = Uri.parse(("android.resource://" + context.applicationContext.packageName) + "/" + R.raw.weather_alarm)

//    return MediaPlayer.create(context.getApplicationContext(), alarmSound)
//
//}
//    private fun startMP(context: Context) {
//        if (mp == null) {
//            mp = MediaPlayer.create(context, R.raw.weather_alarm)
//            mp?.setOnCompletionListener(OnCompletionListener { stopMP(context) })
//        }
//        mp?.start()
//        mp?.setLooping(true)
//    }
//
//    private fun stopMP(context: Context) {
//        if (mp != null) {
//            mp?.stop()
//            mp = null
//            Toast.makeText(context, "song is stopped ", Toast.LENGTH_SHORT).show()
//        }
//    }
 fun ringtone(context: Context,isOn:Boolean) {

    try {

        if (isOn)
        {
            r.play()
        }
        else
        {
            r.stop()
        }
    } catch (e: Exception) {
        e.printStackTrace()
    }
}
}

