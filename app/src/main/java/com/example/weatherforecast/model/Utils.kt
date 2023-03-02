package com.example.weatherforecast.model

import android.content.Context
import android.location.Geocoder
import android.util.Log
import java.text.SimpleDateFormat
import java.util.*

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
    fun getAddressEnglish(context: Context, lat: Double?, lon: Double?):String?{
        Log.i(TAG, "getAddressEnglish: "+lat)
        Log.i(TAG, "getAddressEnglish: $lon")
        val geocoder= Geocoder(context)
        val address =geocoder.getFromLocation(lat!!,lon!!,1)
        return address?.get(0)?.countryName.toString()+" , "+address?.get(0)?.adminArea
    }
    fun getAddressArabic(context: Context,lat:Double,lon:Double):String{
        Log.i(TAG, "getAddressara: "+lat)
        Log.i(TAG, "getAddressara: $lon")
        val geocoder= Geocoder(context,Locale("ar"))
        val address =geocoder.getFromLocation(lat,lon,1)
        return address?.get(0)?.countryName.toString()+" , "+address?.get(0)?.adminArea
    }
}
