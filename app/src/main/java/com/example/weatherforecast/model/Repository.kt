package com.example.weatherforecast.model

import android.content.Context
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.kotlinproducts.view.API
import com.example.weatherforecast.model.Pojos.Constants
import com.example.weatherforecast.model.Pojos.Settings
import com.example.weatherforecast.model.Pojos.Welcome
import com.example.weatherforecast.model.SharedPrefrences.SharedManger
import com.example.weatherforecast.model.database.WeatherDataBse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class Repository private constructor(var context: Context){
    private var room:WeatherDataBse
    companion object{
        @Volatile
        private var INSTANCE: Repository? = null
        fun getInstance (ctx: Context): Repository{
            return INSTANCE ?: synchronized(this) {
                val instance =Repository(ctx)
                INSTANCE = instance
// return instance
                instance }
        }
    }
    init {
        room=WeatherDataBse.getInstance(context)
    }
    //retrofit
    suspend fun getCurrentWeather(lat: String?, lon: String?, lang:String=Constants.LANG_EN, units:String= Constants.UNITS_DEFAULT)=
        flow{
       emit(API.retrofitService.getCurrentWeather(lat = lat, lon =lon, lang =lang, units = units))
    }

    //shared
    fun saveSettings(settings: Settings){
        SharedManger.init(context)
        SharedManger.saveSettings(settings)
    }
    fun getSettings():Settings?{
        SharedManger.init(context)
       return SharedManger.getSettings()
    }
    //room

    suspend fun getCurrentWeather(isCurrent: Boolean) = flow{
        emit(room.getWeatherDao().getCurrentWeather())
    }

    suspend fun getFavoriteWeathers(isFavorite: Boolean) = flow{
        emit(room.getWeatherDao().getFavoriteWeathers())
    }

    suspend fun insertCurrentWeather( welcome: Welcome):Long{
     return   room.getWeatherDao().insertCurrentWeather(welcome)
    }

    suspend fun insertFavoriteWeather( welcome: Welcome):Long{
      return  room.getWeatherDao().insertFavoriteWeather(welcome)
    }

    suspend fun deleteFavorite(welcome: Welcome){
        room.getWeatherDao().deleteFavorite(welcome)
    }
}