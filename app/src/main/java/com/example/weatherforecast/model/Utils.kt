package com.example.weatherforecast.model

import android.app.AlarmManager
import android.app.PendingIntent
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.location.Address
import android.location.Geocoder
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import com.example.weatherforecast.model.Pojos.Constants
import com.example.weatherforecast.ui.notifications.AlarmReciver
import java.text.SimpleDateFormat
import java.util.*
import kotlin.random.Random.Default.nextInt

private const val TAG = "Utils"
object Utils {
    fun getIconUrl(iconCode:String):String{
        return  "https://openweathermap.org/img/wn/" + iconCode + "@4x.png";
    }
    fun formatTime(dateObject: Long): String {

        val date = Date(dateObject * 1000L)
        val sdf = SimpleDateFormat("HH:mm")
        return sdf.format(date)
    }
    fun formatTimeArabic(dateObject: Long): String {

        val date = Date(dateObject * 1000L)
        val sdf = SimpleDateFormat("HH:mm",Locale("ar"))
        return sdf.format(date)
    }
    fun formatDate(dt:Long):String{
        val date= Date(dt * 1000L)
        val sdf = SimpleDateFormat("dd-MM-yyyy")
        return sdf.format(date)

    }
    fun formatDateAlert(dt:Long):String{
        val date= Date(dt )
        val sdf = SimpleDateFormat("dd-MM-yyyy")
        return sdf.format(date)

    }
    fun formatTimeAlert(dt:Long):String{
        val date = Date(dt)
        val sdf = SimpleDateFormat("HH:mm")
        return sdf.format(date)

    }

    fun formatDateArabic(dt:Long):String {
        val date= Date(dt * 1000L)
        val sdf = SimpleDateFormat("dd-MM-yyyy",Locale("ar"))
        return sdf.format(date)
    }

    fun formatday(dt:Long):String{
        val date= Date(dt * 1000L)
        val sdf = SimpleDateFormat("EEEE")
        return sdf.format(date)

    }
    fun formatdayArabic(dt:Long):String{
        val date= Date(dt * 1000L)
        val sdf = SimpleDateFormat("EEEE",Locale("ar"))
        return sdf.format(date)

    }
    fun englishNumberToArabicNumber(number: String): String {
        val arabicNumber = mutableListOf<String>()
        for (element in number.toString()) {
            when (element) {
                '1' -> arabicNumber.add("١")
                '2' -> arabicNumber.add("٢")
                '3' -> arabicNumber.add("٣")
                '4' -> arabicNumber.add("٤")
                '5' -> arabicNumber.add("٥")
                '6' -> arabicNumber.add("٦")
                '7' -> arabicNumber.add("٧")
                '8' -> arabicNumber.add("٨")
                '9' -> arabicNumber.add("٩")
                '0' ->arabicNumber.add("٠")
                '.'->arabicNumber.add(".")
                '-'->arabicNumber.add("-")
                else -> arabicNumber.add(".")
            }
        }
        return arabicNumber.toString()
            .replace("[", "")
            .replace("]", "")
            .replace(",", "")
            .replace(" ", "")
    }
    fun getAddressEnglish(context: Context, lat: Double?, lon: Double?):String{

        var address:MutableList<Address>?=null

            val geocoder= Geocoder(context)
            address =geocoder.getFromLocation(lat!!,lon!!,1)
        if (address?.isEmpty()==true)
        {
            return "Unkown location"
        }
        else
            return address?.get(0)?.countryName.toString()+" , "+address?.get(0)?.adminArea
    }
    fun getAddressArabic(context: Context,lat:Double,lon:Double):String{
        var address:MutableList<Address>?=null

            val geocoder= Geocoder(context,Locale("ar"))
            address =geocoder.getFromLocation(lat,lon,1)

        if (address?.isEmpty()==true)
        {
            return "Unkown location"
        }
        else
            return address?.get(0)?.countryName.toString()+" , "+address?.get(0)?.adminArea

    }
    fun getCurrentDate(): String {
        val currentTime = Calendar.getInstance().time
        val sdf = SimpleDateFormat("dd-MM-yyyy")
        return sdf.format(currentTime)
    }
    fun getCurrentTime(): String {
        val currentTime = Calendar.getInstance().time
        val sdf = SimpleDateFormat("HH:mm")
        return sdf.format(currentTime)
    }
    fun getCurrentDatePlusOne(): String {
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.DAY_OF_YEAR, 1)
        val tomorrow = calendar.time
        val sdf = SimpleDateFormat("dd-MM-yyyy")
        return sdf.format(tomorrow)
    }
    fun pickedDateFormatDate(dt:Date): String {
        val sdf = SimpleDateFormat("dd-MM-yyyy")
        return sdf.format(dt)
    }
    fun pickedDateFormatTime(dt:Date): String {
        val sdf = SimpleDateFormat("HH:mm")
        return sdf.format(dt)
    }

    fun generateRandomNumber():Int{
        return nextInt()
    }
    fun progressDialog(context: Context): ProgressDialog {
      var progressDialog = ProgressDialog(context)
        progressDialog.setTitle("Please Wait")
        progressDialog.setMessage("Loading ...")
        progressDialog.setCancelable(false) // blocks UI interaction
        return progressDialog
    }
    @RequiresApi(Build.VERSION_CODES.M)
    fun canelAlarm(context: Context, alert:String?, requestCode:Int) {
        var alarmMgr: AlarmManager? = null
        lateinit var alarmIntent: PendingIntent

        alarmMgr = context.applicationContext.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmIntent = Intent(context.applicationContext, AlarmReciver::class.java).putExtra(
            Constants.Alert,alert).let { intent ->
            PendingIntent.getBroadcast(context.applicationContext, requestCode, intent,
                PendingIntent.FLAG_IMMUTABLE
            )
        }
        alarmMgr?.cancel(alarmIntent)

    }
    fun isDaily(startTime: Long,endTime:Long):Boolean{
        return endTime-startTime >= 86400000
    }
    @RequiresApi(Build.VERSION_CODES.M)
    fun isOnline(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (connectivityManager != null) {
            val capabilities =
                connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
            if (capabilities != null) {
                if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
                    Log.i("Internet", "NetworkCapabilities.TRANSPORT_CELLULAR")
                    return true
                } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
                    Log.i("Internet", "NetworkCapabilities.TRANSPORT_WIFI")
                    return true
                } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)) {
                    Log.i("Internet", "NetworkCapabilities.TRANSPORT_ETHERNET")
                    return true
                }
            }
        }
        return false
    }

}
