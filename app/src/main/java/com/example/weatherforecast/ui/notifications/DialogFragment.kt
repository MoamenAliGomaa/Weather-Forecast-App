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
import android.view.Window
import android.widget.TextView
import android.widget.TimePicker
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.NavHostFragment
import androidx.work.*
import com.example.kotlinproducts.view.API
import com.example.weatherforecast.databinding.AlertDialogFragmentBinding
import com.example.weatherforecast.model.Network.RemoteDataSource
import com.example.weatherforecast.model.Pojos.Alert
import com.example.weatherforecast.model.Pojos.Constants
import com.example.weatherforecast.model.Repository
import com.example.weatherforecast.model.SharedPrefrences.SharedManger
import com.example.weatherforecast.model.Utils
import com.example.weatherforecast.model.Utils.getCurrentDate
import com.example.weatherforecast.model.Utils.getCurrentTime
import com.example.weatherforecast.model.database.LocalDataSource
import com.example.weatherforecast.model.database.WeatherDataBse
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

    override fun onStart() {
        super.onStart()
        val window: Window? = dialog!!.window
        window?.setBackgroundDrawableResource(android.R.color.transparent)
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

        getDialog()?.requestWindowFeature(STYLE_NO_TITLE)
        setCancelable(false)
        SharedManger.init(requireContext())
        var repository= Repository.getInstance(LocalDataSource(requireContext()),RemoteDataSource())
        val notificationsViewModel =
            ViewModelProvider(this,NotificationsViewModelFactory(repository)).get(NotificationsViewModel::class.java)
        var alertSettings=notificationsViewModel.getAlertSettings()
        calendarStart=Calendar.getInstance()
        calenderEnd=Calendar.getInstance()
        binding.tvFromDate.text= getCurrentDate()
        binding.tvToDate.text= getCurrentDate()
        binding.tvFromTimePicker.text= getCurrentTime()
        binding.tvToTimePicker.text=getCurrentTime()
        if(Utils.isOnline(requireContext()))
        {
            binding.btnMap.isEnabled=true
            binding.btnSet.isEnabled=true
        }
        else

        {
            binding.btnMap.isEnabled=false
            binding.btnSet.isEnabled=false
            Toast.makeText(requireContext(),"Please enable your network connection ",Toast.LENGTH_SHORT).show()
        }
        if(alertSettings?.isALarm == true&&alertSettings.isNotification==false)
        {
         binding.radioButtonAlarm.isChecked=true
        }
        if(alertSettings?.isALarm == false && alertSettings.isNotification)
        {
            binding.radioButtonNotify.isChecked=true
        }



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
            if (alert.startTime<alert.endTime){
                if (binding.radioButtonAlarm.isChecked)
                {
                    alertSettings?.isALarm=true
                    alertSettings?.isNotification=false
                }
                if (binding.radioButtonNotify.isChecked){
                    alertSettings?.isALarm=false
                    alertSettings?.isNotification=true
                }
                notificationsViewModel.saveAlertSettings(alertSettings)
                notificationsViewModel.insertAlert(alert)
                Log.e(TAGA, "onCreateView: "+notificationsViewModel.getAlertSettings().toString())

                val inputData = Data.Builder()
                inputData.putString(Constants.Alert,  Gson().toJson(alert).toString() )

                // Create a Constraints that defines when the task should run
                val myConstraints: Constraints = Constraints.Builder()
                    .setRequiresDeviceIdle(false)
                    .setRequiresCharging(false)
                    .setRequiredNetworkType(NetworkType.CONNECTED)
                    .build()
                Toast.makeText(context,Utils.formatTimeAlert(alert.startTime)+" "+Utils.formatTimeAlert(alert.endTime),Toast.LENGTH_SHORT).show()

                    Toast.makeText(context,"Daily",Toast.LENGTH_SHORT).show()

                    val myWorkRequest= PeriodicWorkRequestBuilder<MyWorker>(1,TimeUnit.DAYS).setConstraints(myConstraints).
                    setInputData(inputData.build()).
                    addTag(alert.startTime.toString()).
                    build()
                    WorkManager.getInstance(requireContext().applicationContext).enqueueUniquePeriodicWork(alert.startTime.toString(), ExistingPeriodicWorkPolicy.REPLACE, myWorkRequest)

                dismiss()
            }
            else{
                Toast.makeText(context,"Please specify the end time of your alert",Toast.LENGTH_SHORT).show()

            }

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

            }, startHour, startMinute, false).show()
        }, startYear, startMonth, startDay).show()
    }


}