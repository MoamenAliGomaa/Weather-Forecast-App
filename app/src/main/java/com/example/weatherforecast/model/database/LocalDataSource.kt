package com.example.weatherforecast.model.database

import android.content.Context
import androidx.room.*
import com.example.weatherforecast.model.Pojos.Alert
import com.example.weatherforecast.model.Pojos.Welcome
import kotlinx.coroutines.flow.Flow

class LocalDataSource(var context: Context) : ILocalDataSource {
  var room:WeatherDataBse =WeatherDataBse.getInstance(context)
  init {
    room=WeatherDataBse.getInstance(context)
  }

  override fun getFavoriteWeathers(): Flow<List<Welcome>?>{
   return room.getWeatherDao().getFavoriteWeathers()
  }

  override suspend fun insertWeather(welcome: Welcome): Long
  {
    return room.getWeatherDao().insertWeather(welcome)

  }
  override suspend fun deleteFavorite(welcome: Welcome):Int{
    return room.getWeatherDao().deleteFavorite(welcome)
  }

  override suspend fun deleteCurrent():Int{
    return room.getWeatherDao().deleteCurrent()
  }

  override suspend fun insertCurrentWeather(welcome: Welcome): Long
  {
    return room.getWeatherDao().insertCurrentWeather(welcome)
  }

  override fun getCurrentWeathers(): Flow<Welcome>?{
    return room.getWeatherDao().getCurrentWeathers()
  }

  override suspend fun insertOrUpdateCurrentWeather(welcome: Welcome)
  {
    return room.getWeatherDao().insertOrUpdateCurrentWeather(welcome)
  }

  override fun getAlerts(): Flow<List<Alert>>{
    return room.alertDao().getAlerts()
  }

  override fun getAlert(id:Long): Flow<Alert>{
    return room.alertDao().getAlert(id)
  }

  override suspend fun insertAlert(alert: Alert): Long{
    return room.alertDao().insertAlert(alert)
  }

  override suspend fun deleteAlert(alert: Alert){
    return room.alertDao().deleteAlert(alert)
  }
}