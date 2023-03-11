package com.example.weatherforecast.ui.notifications


import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.PendingIntent.FLAG_MUTABLE
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.media.AudioManager.STREAM_ALARM
import android.media.AudioManager.STREAM_RING
import android.media.RingtoneManager
import android.net.Uri
import android.os.Build
import android.provider.Settings
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.example.weatherforecast.R
import com.example.weatherforecast.model.Pojos.Constants


class NotificationHelper(context: Context?) : ContextWrapper(context) {
    private var mNotificationManager: NotificationManager? = null
    private val MY_CHANNEL = "my_channel"
    private val vibrationScheme = longArrayOf(200, 400)

    /**
     * Registers notification channels, which can be used later by individual notifications.
     *
     * @param context The application context
     */
    init {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            // Create the channel object with the unique ID MY_CHANNEL
            val myChannel = NotificationChannel(
                MY_CHANNEL,
                resources.getString(R.string.notification_channel_title),
                NotificationManager.IMPORTANCE_HIGH
            )

            // Configure the channel's initial settings
            myChannel.lightColor = Color.GREEN
            myChannel.vibrationPattern = vibrationScheme

            // Submit the notification channel object to the notification manager
            notificationManager!!.createNotificationChannel(myChannel)
        }
    }

    /**
     * Build you notification with desired configurations
     *
     */
    fun getNotificationBuilder(
        title: String?,
        body: String?,
        context:Context
    ): NotificationCompat.Builder {
        val contentIntent = PendingIntent.getActivity(
            context, 0,
            Intent(context, AlarmReciver::class.java), FLAG_MUTABLE
        )
        val notificationLargeIconBitmap = BitmapFactory.decodeResource(
            applicationContext.resources,
            R.mipmap.ic_launcher
        )
        return NotificationCompat.Builder(applicationContext, MY_CHANNEL)
            .setSmallIcon(R.drawable.humidity)
            .setLargeIcon(notificationLargeIconBitmap)
            .setContentTitle(title)
            .setContentText(body)
            .setSound(Settings.System.DEFAULT_NOTIFICATION_URI)
            .setVibrate(vibrationScheme)
            .setStyle(NotificationCompat.BigTextStyle().bigText(body))
            .setColor(ContextCompat.getColor(applicationContext, R.color.colorPrimary))
            .setContentIntent(contentIntent)
            .setAutoCancel(true)
    }

    val notificationManager: NotificationManager?
        get() {
            if (mNotificationManager == null) {
                mNotificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            }
            return mNotificationManager
        }
    @RequiresApi(Build.VERSION_CODES.S)
    fun alarmNotificationManager(context: Context): NotificationManager {
        val channel = NotificationChannel(
            MY_CHANNEL, resources.getString(R.string.notification_channel_title),
            NotificationManager.IMPORTANCE_HIGH
        ).apply {
            description="description"

        }
        // for heads-up notifications

        // Register channel with system
        // Register channel with system
        val notificationManager: NotificationManager =
            context.getSystemService(NotificationManager::class.java)
        notificationManager!!.createNotificationChannel(channel)
        return notificationManager
    }
fun getNotification(context: Context,notificationId: Int,title:String,description: String,bitmap:Bitmap):Notification{
    val fullScreenIntent =
        Intent(context.applicationContext, FullScreenActivity::class.java).apply {
            action=Constants.ACTION_SNOOZE
            putExtra(Constants.EXTRA_NOTIFICATION_ID,notificationId)
        }
    val fullScreenPendingIntent = PendingIntent.getActivity(
        context.applicationContext, 0,
        fullScreenIntent, FLAG_MUTABLE
    )
    val snoozeIntent = Intent(this, ActionReceiver::class.java).apply {
        action = Constants.ACTION_SNOOZE
        putExtra(Constants.EXTRA_NOTIFICATION_ID, notificationId)
    }
    val snoozePendingIntent: PendingIntent =
        PendingIntent.getBroadcast(this, 0, snoozeIntent, FLAG_MUTABLE)

    val notification: Notification =
        NotificationCompat.Builder(context, MY_CHANNEL)
            .setSmallIcon(R.drawable.weather_app_small)
            .setContentTitle(title)
            .setContentText(description)
            .setDefaults(Notification.DEFAULT_ALL)
            .setPriority(NotificationCompat.PRIORITY_MAX)
            .setCategory(Notification.CATEGORY_ALARM)
            .setLargeIcon(bitmap)
            .addAction(R.drawable.app_icon, "Dismiss", snoozePendingIntent)
            .setFullScreenIntent(fullScreenPendingIntent,true)
            .setOngoing(true)
            .build()
    return notification
}

}

