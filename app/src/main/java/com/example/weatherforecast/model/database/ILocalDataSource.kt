package com.example.weatherforecast.model.database

import com.example.weatherforecast.model.Pojos.Alert
import com.example.weatherforecast.model.Pojos.Welcome
import kotlinx.coroutines.flow.Flow

interface ILocalDataSource {
    fun getFavoriteWeathers(): Flow<List<Welcome>?>
    suspend fun insertWeather(welcome: Welcome): Long

    suspend fun deleteFavorite(welcome: Welcome): Int
    suspend fun deleteCurrent(): Int
    suspend fun insertCurrentWeather(welcome: Welcome): Long

    fun getCurrentWeathers(): Flow<Welcome>?
    suspend fun insertOrUpdateCurrentWeather(welcome: Welcome)

    fun getAlerts(): Flow<List<Alert>>
    fun getAlert(id: Long): Flow<Alert>
    suspend fun insertAlert(alert: Alert): Long
    suspend fun deleteAlert(alert: Alert)
}