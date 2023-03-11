package com.example.weatherforecast.ui.notifications


import android.app.AlertDialog
import android.app.TimePickerDialog
import android.content.ContentValues.TAG

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TimePicker
import androidx.annotation.RequiresApi

import androidx.fragment.app.Fragment

import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.fragment.findNavController
import com.example.weatherforecast.R
import com.example.weatherforecast.databinding.FragmentNotificationsBinding
import java.util.*


class NotificationsFragment : Fragment() {
    lateinit var timePicker: TimePicker
    lateinit var timePickerDialog: TimePickerDialog

    val RQS_1 = 1
    private var _binding: FragmentNotificationsBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNotificationsBinding.inflate(inflater, container, false)
        val root: View = binding.root
        val notificationsViewModel =
            ViewModelProvider(this,NotificationsViewModelFactory(requireContext())).get(NotificationsViewModel::class.java)
        canDrawOverApps()

        binding.btnAddToAlerts.setOnClickListener{
            AlertDialogFragment().show(
                childFragmentManager, AlertDialogFragment.TAG)
        }
//        binding.startSetDialog.setOnClickListener(View.OnClickListener {
//            binding.alarmprompt.setText("")
//            openTimePickerDialog(false)
//        })



        return root
    }
//    private fun openTimePickerDialog(is24r: Boolean) {
//        val calendar: Calendar = Calendar.getInstance()
//        timePickerDialog = TimePickerDialog(
//            requireContext(),
//            onTimeSetListener,
//            calendar.get(Calendar.HOUR_OF_DAY),
//            calendar.get(Calendar.MINUTE),
//            is24r
//        )
//        timePickerDialog.setTitle("Set Alarm Time")
//        timePickerDialog.show()
//    }
//    var onTimeSetListener =
//        OnTimeSetListener { view, hourOfDay, minute ->
//            val calNow = Calendar.getInstance()
//            val calSet = calNow.clone() as Calendar
//            calSet[Calendar.HOUR_OF_DAY] = hourOfDay
//            calSet[Calendar.MINUTE] = minute
//            calSet[Calendar.SECOND] = 0
//            calSet[Calendar.MILLISECOND] = 0
//            if (calSet.compareTo(calNow) <= 0) {
//                //Today Set time passed, count to tomorrow
//                calSet.add(Calendar.DATE, 1)
//            }
//            setAlarm(calSet)
//        }
//    private fun setAlarm(targetCal: Calendar) {
//        binding.alarmprompt.setText(
//            """
//
//
//            ***
//            Alarm is set@ ${targetCal.time}
//            ***
//
//            """.trimIndent()
//        )
//        val intent = Intent(requireContext(), AlarmReciver::class.java)
//        val pendingIntent = PendingIntent.getBroadcast(requireContext(), RQS_1, intent, FLAG_IMMUTABLE)
//        val alarmManager = getSystemService(requireContext(),AlarmManager::class.java)
//        alarmManager!![AlarmManager.RTC_WAKEUP, targetCal.timeInMillis] = pendingIntent
//    }
    @RequiresApi(Build.VERSION_CODES.M)
    fun canDrawOverApps(){
        if (!Settings.canDrawOverlays(requireContext())) {
            val intent = Intent(
                Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                Uri.parse("package:" + activity?.packageName)
            )
            startActivityForResult(intent, 0)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}