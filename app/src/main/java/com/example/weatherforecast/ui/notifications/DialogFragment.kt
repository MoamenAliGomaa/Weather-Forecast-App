package com.example.weatherforecast.ui.notifications


import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.TimePicker
import androidx.annotation.RequiresApi
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.NavHostFragment
import androidx.work.*
import com.example.weatherforecast.databinding.AlertDialogFragmentBinding
import com.example.weatherforecast.model.Pojos.Alert
import com.example.weatherforecast.model.Pojos.Constants
import com.example.weatherforecast.model.Utils
import com.example.weatherforecast.model.Utils.getCurrentDate
import com.example.weatherforecast.model.Utils.getCurrentDatePlusOne
import com.example.weatherforecast.model.Utils.getCurrentTime
import com.google.gson.Gson
import java.util.*
import java.util.concurrent.TimeUnit


private const val TAGA = "DialogFragment"
class AlertDialogFragment : DialogFragment() {

    private var _binding: AlertDialogFragmentBinding? = null
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    lateinit var timePicker: TimePicker
    lateinit var timePickerDialog: TimePickerDialog
    lateinit var calendarStart: Calendar
    lateinit var calenderEnd: Calendar

    val RQS_1 = 1

    companion object {
        const val TAG = "DialogFragment"
    }
    @SuppressLint("IdleBatteryChargingConstraints")
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = AlertDialogFragmentBinding.inflate(inflater, container, false)
        val root: View = binding.root


        val notificationsViewModel =
            ViewModelProvider(this,NotificationsViewModelFactory(requireContext())).get(NotificationsViewModel::class.java)
        var alertSettings=notificationsViewModel.getAlertSettings()
        calendarStart=Calendar.getInstance()
        calenderEnd=Calendar.getInstance()
        binding.tvFromDate.text= getCurrentDate()
        binding.tvToDate.text= getCurrentDatePlusOne()
        binding.tvFromTimePicker.text= getCurrentTime()
        binding.tvToTimePicker.text=getCurrentTime()
        binding.tvFromTimePicker.setOnClickListener {
            pickDateTime(binding.tvFromDate,binding.tvFromTimePicker,calendarStart)
        }
        binding.tvToTimePicker.setOnClickListener {

            pickDateTime(binding.tvToDate,binding.tvToTimePicker,calenderEnd)
        }

        binding.btnMap.text=Utils.getAddressEnglish(requireContext(),alertSettings?.lat,alertSettings?.lon)
        binding.btnMap.setOnClickListener {
            val action=
                NotificationsFragmentDirections.actionNavigationNotificationsToMapsFragment2(false,false,true)
            NavHostFragment.findNavController(this).navigate(action)
        }


        binding.btnSet.setOnClickListener {
            Log.e(TAGA, "onCreateView: start Date "+
                    Utils.formatDateAlert(calendarStart.timeInMillis)+" start time "+Utils.formatTimeAlert(calendarStart.timeInMillis)+
            " end date " +Utils.formatDateAlert(calenderEnd.timeInMillis)+" end time "+Utils.formatTimeAlert(calenderEnd.timeInMillis))
            var alert=Alert(
                startTime=calendarStart.timeInMillis,
                endTime=calenderEnd.timeInMillis,
                lat = alertSettings!!.lat,
                lon = alertSettings.lon,
                cityName = Utils.getAddressEnglish(requireContext(),alertSettings!!.lat,alertSettings.lon)
            )
            notificationsViewModel.insertAlert(alert)
            Log.e(TAGA, "onCreateView: "+notificationsViewModel.getAlertSettings().toString())

            val inputData = Data.Builder()
            inputData.putString(Constants.Alert,  Gson().toJson(alert).toString() )
            // Create a Constraints that defines when the task should run
            // Create a Constraints that defines when the task should run
            val myConstraints: Constraints = Constraints.Builder()
                .setRequiresDeviceIdle(false)
                .setRequiresCharging(false)
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build()

            val myWorkRequest= PeriodicWorkRequestBuilder<MyWorker>(1,TimeUnit.DAYS).setConstraints(myConstraints).setInputData(inputData.build()).addTag(Constants.Alert).build()
            WorkManager.getInstance(requireContext()).enqueue(myWorkRequest)

            dismiss()

        }
        binding.btnCancel.setOnClickListener {
            dismiss()
        }

        return root

    }
    private fun pickDateTime(tvdate:TextView,tvTime:TextView, calendar: Calendar) {
        val currentDateTime = Calendar.getInstance()
        val startYear = currentDateTime.get(Calendar.YEAR)
        val startMonth = currentDateTime.get(Calendar.MONTH)
        val startDay = currentDateTime.get(Calendar.DAY_OF_MONTH)
        val startHour = currentDateTime.get(Calendar.HOUR_OF_DAY)
        val startMinute = currentDateTime.get(Calendar.MINUTE)
        var pickedDateTime:Calendar
        DatePickerDialog(requireContext(), DatePickerDialog.OnDateSetListener { _, year, month, day ->
            TimePickerDialog(requireContext(), TimePickerDialog.OnTimeSetListener { _, hour, minute ->
                pickedDateTime = Calendar.getInstance()
                pickedDateTime.set(year, month, day, hour, minute)
                tvdate.text=Utils.pickedDateFormatDate(pickedDateTime.time)
                tvTime.text=Utils.pickedDateFormatTime(pickedDateTime.time)
                calendar.time=pickedDateTime.time

            }, startHour, startMinute, true).show()
        }, startYear, startMonth, startDay).show()
    }

}