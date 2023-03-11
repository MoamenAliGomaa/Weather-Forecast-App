package com.example.weatherforecast.model

import android.content.Context
import android.util.Log
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.kotlinproducts.view.API
import com.example.weatherforecast.model.Pojos.*
import com.example.weatherforecast.model.SharedPrefrences.SharedManger
import com.example.weatherforecast.model.database.WeatherDataBse
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*

private const val TAG = "Repository"
class Repository private constructor(var context: Context){
    private var room:WeatherDataBse
    private  var currentList: MutableStateFlow<LocalDataState> = MutableStateFlow(LocalDataState.Loading)

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
    fun saveAlertSettings(alertSettings: AlertSettings){
        SharedManger.init(context)
        SharedManger.saveAlertSettings(alertSettings)
    }
    fun getAlertSettings():AlertSettings?{
        SharedManger.init(context)
        return SharedManger.getAlertSettings()
    }
    //room

   suspend fun getCurrentWeatherDataBase() = room.getWeatherDao().getCurrentWeather()
   fun getFavoriteWeathers() = room.getWeatherDao().getFavoriteWeathers()



    suspend fun insertWeather( welcome: Welcome):Long{
      return  room.getWeatherDao().insertWeather(welcome)
    }


    suspend fun deleteFavorite(welcome: Welcome){
        room.getWeatherDao().deleteFavorite(welcome)
    }
    //    suspend fun updateCurrent(welcome: Welcome)
//    {
//        room.getWeatherDao().updateCurrent(welcome.lat,welcome.lon,welcome.timezone,welcome.timezone_offset,welcome.current,welcome.hourly,
//            welcome.daily,welcome.isFavorite)
//    }
//    suspend fun insertOrUpdate(welcome: Welcome){
//
//        var currentList=getCurrentWeatherDataBase()
//        if(currentList.isNullOrEmpty())
//        {   welcome.isCurrent=true
//            insertWeather(welcome)
//        }
//        else
//        {
//         updateCurrent(welcome)
//        }
//    }
    //alert room
    suspend fun insertAlert(alert: Alert)=room.alertDao().insertAlert(alert)
    suspend fun deleteAlert(alert: Alert)=room.alertDao().deleteAlert(alert)
    fun getAlerts()=room.alertDao().getAlerts()
}