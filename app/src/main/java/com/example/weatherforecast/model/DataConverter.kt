package com.example.weatherforecast.model

import androidx.room.TypeConverter
import com.example.weatherforecast.model.Pojos.Current
import com.example.weatherforecast.model.Pojos.Daily
import com.example.weatherforecast.model.Pojos.Weather
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type


class DataConverter {
    @TypeConverter
    fun frowWeatherList(weather: List<Weather>?): String? {
        if (weather == null) {
            return null
        }
        val gson = Gson()
        val type: Type = object : TypeToken<List<Weather>?>() {}.type
        return gson.toJson(weather, type)
    }

    @TypeConverter
    fun toWeatherList(weatherString: String?): List<Weather>? {
        if (weatherString == null) {
            return null
        }
        val gson = Gson()
        val type: Type = object : TypeToken<List<Weather>?>() {}.type
        return gson.fromJson(weatherString, type)
    }
    @TypeConverter
    fun fromCurrentList(hourly: List<Current>?): String? {
        if (hourly == null) {
            return null
        }
        val gson = Gson()
        val type: Type = object : TypeToken<List<Current>?>() {}.type
        return gson.toJson(hourly, type)
    }

    @TypeConverter
    fun toCurrentList(currentString: String?): List<Current>? {
        if (currentString == null) {
            return null
        }
        val gson = Gson()
        val type: Type = object : TypeToken<List<Current>?>() {}.type
        return gson.fromJson(currentString, type)
    }
    @TypeConverter
    fun fromDailyList(daily: List<Daily?>?): String? {
        if (daily == null) {
            return null
        }
        val gson = Gson()
        val type: Type = object : TypeToken<List<Daily?>?>() {}.type
        return gson.toJson(daily, type)
    }

    @TypeConverter
    fun toDailyList(dailyString: String?): List<Daily>? {
        if (dailyString == null) {
            return null
        }
        val gson = Gson()
        val type: Type = object : TypeToken<List<Daily?>?>() {}.type
        return gson.fromJson(dailyString, type)
    }
    @TypeConverter
    fun fromcurrent(current: Current?): String? {
        if (current == null) {
            return null
        }
        val gson = Gson()
        val type: Type = object : TypeToken<Current>() {}.type
        return gson.toJson(current, type)
    }

    @TypeConverter
    fun toCurrent(currentStr: String?): Current? {
        if (currentStr == null) {
            return null
        }
        val gson = Gson()
        val type: Type = object : TypeToken<Current>() {}.type
        return gson.fromJson(currentStr, type)
    }
}