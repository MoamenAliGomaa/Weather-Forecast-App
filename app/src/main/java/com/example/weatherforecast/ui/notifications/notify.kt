package com.example.weatherforecast.ui.notifications


import android.media.MediaPlayer
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.work.WorkManager
import com.example.weatherforecast.R
import com.example.weatherforecast.databinding.ActivityFullScreenBinding
import com.example.weatherforecast.databinding.AlertDialogFragmentBinding
import com.example.weatherforecast.model.Pojos.Alert
import com.example.weatherforecast.model.Pojos.Constants
import com.example.weatherforecast.model.Utils
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class FullScreenActivity : AppCompatActivity() {
    var r=com.example.weatherforecast.ui.notifications.AlarmReciver.r
    private var _binding: ActivityFullScreenBinding? = null
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding
    @RequiresApi(Build.VERSION_CODES.S)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityFullScreenBinding.inflate(layoutInflater)
        setContentView(binding?.root)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O_MR1) {
            setShowWhenLocked(true);
            setTurnScreenOn(true);
        }
        var notificationHelper=NotificationHelper(applicationContext)
        binding?.btnDismissFullScreenIntent?.setOnClickListener {
                r.stop()
                notificationHelper.alarmNotificationManager(applicationContext).cancel(1)
                finish()

        }


    }
}
