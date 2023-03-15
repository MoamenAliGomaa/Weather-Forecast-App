package com.example.weatherforecast.DataSource

import com.example.kotlinproducts.view.API
import com.example.weatherforecast.model.IRepository
import com.example.weatherforecast.model.Network.IRemoteDataSource
import com.example.weatherforecast.model.Pojos.Alert
import com.example.weatherforecast.model.Pojos.AlertSettings
import com.example.weatherforecast.model.Pojos.Settings
import com.example.weatherforecast.model.Pojos.Welcome
import com.example.weatherforecast.model.database.ILocalDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class FakeTestRepositary(private var localDataSource: ILocalDataSource,private var remoteDataSource: IRemoteDataSource):IRepository {

    override suspend fun getCurrentWeather(
        lat: String?,
        lon: String?,
        lang: String,
        units: String
    ): Flow<Welcome> {
        return flowOf(remoteDataSource.getCurrentWeather(lat,lon,"",lang,units))
    }

    override fun saveSettings(settings: Settings) {
        TODO("Not yet implemented")
    }

    override fun getSettings(): Settings? {
        TODO("Not yet implemented")
    }

    override fun saveAlertSettings(alertSettings: AlertSettings) {
        TODO("Not yet implemented")
    }

    override fun getAlertSettings(): AlertSettings? {
        TODO("Not yet implemented")
    }

    override suspend fun getFavoriteWeathers(): Flow<List<Welcome>?> {
      return localDataSource.getFavoriteWeathers()
    }

    override suspend fun insertWeather(welcome: Welcome): Long {
   return localDataSource.insertWeather(welcome)
    }

    override suspend fun deleteFavorite(welcome: Welcome) {
        localDataSource.deleteFavorite(welcome)
    }

    override suspend fun getCurrentWeatherDB(): Flow<Welcome>? {
       return localDataSource.getCurrentWeathers()
    }

    override suspend fun insertOrUpdateCurrentWeather(welcome: Welcome) {
      localDataSource.insertOrUpdateCurrentWeather(welcome)
    }

    override suspend fun updateFavWeather(welcome: Welcome) {
        localDataSource.updateFavWeather(welcome)
    }

    override suspend fun insertAlert(alert: Alert): Long {
      return localDataSource.insertAlert(alert)
    }

    override suspend fun deleteAlert(alert: Alert) {
       localDataSource.deleteAlert(alert)
    }

    override fun getAlerts(): Flow<List<Alert>> {
     return  localDataSource.getAlerts()
    }

    override fun getAlert(id: Long): Flow<Alert> {
      return localDataSource.getAlert(id)
    }
}