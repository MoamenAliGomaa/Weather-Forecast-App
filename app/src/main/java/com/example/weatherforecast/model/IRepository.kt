package com.example.weatherforecast.model

import com.example.weatherforecast.model.Pojos.*
import kotlinx.coroutines.flow.Flow

interface IRepository {
    //retrofit
    suspend fun getCurrentWeather(
        lat: String?,
        lon: String?,
        lang: String = Constants.LANG_EN,
        units: String = Constants.UNITS_DEFAULT
    ): Flow<Welcome>

    //shared
    fun saveSettings(settings: Settings)
    fun getSettings(): Settings?
    fun saveAlertSettings(alertSettings: AlertSettings)
    fun getAlertSettings(): AlertSettings?
    suspend fun getFavoriteWeathers(): Flow<List<Welcome>?>

    suspend fun insertWeather(welcome: Welcome): Long
    suspend fun deleteFavorite(welcome: Welcome)
    suspend fun getCurrentWeatherDB(): Flow<Welcome>?
    suspend fun insertOrUpdateCurrentWeather(welcome: Welcome)

    //alert room
    suspend fun insertAlert(alert: Alert): Long
    suspend fun deleteAlert(alert: Alert)
    fun getAlerts(): Flow<List<Alert>>
    fun getAlert(id: Long): Flow<Alert>
}