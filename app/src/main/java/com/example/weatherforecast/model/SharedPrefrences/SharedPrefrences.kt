package com.example.weatherforecast.model.SharedPrefrences

import android.content.Context
import android.content.SharedPreferences
import android.content.SharedPreferences.Editor
import com.example.weatherforecast.model.Pojos.Settings
import com.google.gson.Gson

private const val SHARE_KEY = "shareRoom"
object SharedManger{
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var editor: Editor
    const val SETTINGS = "SETTINGS"
   fun init(context:Context) {
       sharedPreferences =context.getSharedPreferences(SHARE_KEY, Context.MODE_PRIVATE)

    }
    fun saveSettings(settings: Settings){
        editor= sharedPreferences.edit()
        editor.putString(SETTINGS,Gson().toJson(settings))
        editor.commit()
    }
    fun getSettings():Settings?{
        val settingsStr = sharedPreferences.getString(SETTINGS, null)
        var settings: Settings? = Settings()
        if (settingsStr != null) {
            settings = Gson().fromJson(settingsStr, Settings::class.java)

        }
        return settings
    }

}