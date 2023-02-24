package com.example.weatherforecast.model

import java.text.SimpleDateFormat
import java.util.*

object Utils {
    fun getIconUrl(iconCode:String):String{
        return  "https://openweathermap.org/img/wn/" + iconCode + "@4x.png";
    }
    fun formatTime(dateObject: Long): String {

        val date = Date(dateObject * 1000L)
        val sdf = SimpleDateFormat("HH:mm")
        return sdf.format(date)
    }
    fun formatDate(dt:Long):String{
        val date= Date(dt * 1000L)
        val sdf = SimpleDateFormat("dd-MM-yyyy")
        return sdf.format(date)

    }
    fun formatday(dt:Long):String{
        val date= Date(dt * 1000L)
        val sdf = SimpleDateFormat("EEEE")
        return sdf.format(date)

    }
}
