package com.example.weatherforecast.model

import android.content.Context
import android.util.Log
import com.example.kotlinproducts.view.API
import com.example.kotlinproducts.view.SimpleService
import com.example.weatherforecast.model.Network.IRemoteDataSource
import com.example.weatherforecast.model.Pojos.*
import com.example.weatherforecast.model.SharedPrefrences.SharedManger
import com.example.weatherforecast.model.database.ILocalDataSource
import com.example.weatherforecast.model.database.WeatherDataBse
import kotlinx.coroutines.flow.*
import retrofit2.Retrofit

private const val TAG = "Repository"
class Repository private constructor(var room:ILocalDataSource,var retrofit: IRemoteDataSource) :
    IRepository {
  //  private var room:WeatherDataBse


    companion object{
        @Volatile
        private var INSTANCE: Repository? = null
        fun getInstance (room:ILocalDataSource,retrofit: IRemoteDataSource): Repository{
            return INSTANCE ?: synchronized(this) {
                val instance =Repository(room,retrofit)
                INSTANCE = instance
// return instance
                instance }
        }
    }

    //retrofit
    override suspend fun getCurrentWeather(lat: String?, lon: String?, lang:String, units:String)=
        flow{
       emit(retrofit.getCurrentWeather(lat = lat, lon =lon, lang =lang, units = units))
    }.catch {
            Log.e(TAG, "getCurrentWeather: "+this.toString() )
        }

    //shared
    override fun saveSettings(settings: Settings){
       // SharedManger.init(context)
        SharedManger.saveSettings(settings)
    }
    override fun getSettings():Settings?{
       // SharedManger.init(context)
       return SharedManger.getSettings()
    }
    override fun saveAlertSettings(alertSettings: AlertSettings){
       // SharedManger.init(context)
        SharedManger.saveAlertSettings(alertSettings)
    }
    override fun getAlertSettings():AlertSettings?{
       // SharedManger.init(context)
        return SharedManger.getAlertSettings()
    }
    //room


    override suspend fun getFavoriteWeathers() = room.getFavoriteWeathers()




    override suspend fun insertWeather(welcome: Welcome):Long{
        var l=room.insertWeather(welcome)
        Log.e(TAG, "insertWeather: "+ l )
      return  l
    }


    override suspend fun deleteFavorite(welcome: Welcome){
        room.deleteFavorite(welcome)
    }
    override suspend fun getCurrentWeatherDB()=room.getCurrentWeathers()
    override suspend fun insertOrUpdateCurrentWeather(welcome: Welcome)= room.insertOrUpdateCurrentWeather(welcome)
    override suspend fun updateFavWeather(welcome: Welcome)=room.updateFavWeather(welcome)
    //alert room
    override suspend fun insertAlert(alert: Alert)=room.insertAlert(alert)
    override suspend fun deleteAlert(alert: Alert)=room.deleteAlert(alert)
    override fun getAlerts()=room.getAlerts()
    override fun getAlert(id: Long)=room.getAlert(id!!)


}