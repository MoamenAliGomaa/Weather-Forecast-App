package com.example.weatherforecast.model

import android.content.Context
import android.util.Log
import com.example.kotlinproducts.view.API
import com.example.weatherforecast.model.Pojos.*
import com.example.weatherforecast.model.SharedPrefrences.SharedManger
import com.example.weatherforecast.model.database.WeatherDataBse
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
    }.catch {
            Log.e(TAG, "getCurrentWeather: "+this.toString() )
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


    suspend fun getFavoriteWeathers() = room.getWeatherDao().getFavoriteWeathers()




    suspend fun insertWeather( welcome: Welcome):Long{
        var l=room.getWeatherDao().insertWeather(welcome)
        Log.e(TAG, "insertWeather: "+ l )
      return  l
    }


    suspend fun deleteFavorite(welcome: Welcome){
        room.getWeatherDao().deleteFavorite(welcome)
    }
    suspend fun getCurrentWeatherDB()=room.getWeatherDao().getCurrentWeathers()
    suspend fun insertOrUpdateCurrentWeather(welcome: Welcome)= room.getWeatherDao().insertOrUpdateCurrentWeather(welcome)
    //alert room
    suspend fun insertAlert(alert: Alert)=room.alertDao().insertAlert(alert)
    suspend fun deleteAlert(alert: Alert)=room.alertDao().deleteAlert(alert)
    fun getAlerts()=room.alertDao().getAlerts()
    fun getAlert(id: Long)=room.alertDao().getAlert(id!!)

}