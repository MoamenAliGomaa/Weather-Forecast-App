package com.example.weatherforecast.DataSource

import com.example.weatherforecast.model.Pojos.Alert
import com.example.weatherforecast.model.Pojos.Current
import com.example.weatherforecast.model.Pojos.Welcome
import com.example.weatherforecast.model.database.ILocalDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf


class FakeLocalDataSource(private var alertList: MutableList<Alert> = mutableListOf(),
                          private var welcomeList: MutableList<Welcome> = mutableListOf(),private var current: Welcome):ILocalDataSource {

    override fun getFavoriteWeathers(): Flow<List<Welcome>?> {
     return flowOf(welcomeList)
    }

    override suspend fun insertWeather(welcome: Welcome): Long {
     welcomeList.add(welcome)
        return 1000
    }

    override suspend fun deleteFavorite(welcome: Welcome): Int {
       welcomeList.remove(welcome)
        return 1
    }

    override suspend fun deleteCurrent(): Int {
        welcomeList.clear()
        return 1
    }

    override suspend fun insertCurrentWeather(welcome: Welcome): Long {
        welcomeList.add(welcome)
        return 1000
    }

    override fun getCurrentWeathers(): Flow<Welcome>? {
       return flowOf(current)
    }

    override suspend fun insertOrUpdateCurrentWeather(welcome: Welcome) {
        welcomeList.add(current)
    }

    override fun getAlerts(): Flow<List<Alert>> {
        return flowOf(alertList)
    }

    override fun getAlert(id: Long): Flow<Alert> {

        return flowOf(alertList.get(id.toInt()))
    }

    override suspend fun insertAlert(alert: Alert): Long {
      alertList.add(alert)
        return 1000
    }

    override suspend fun deleteAlert(alert: Alert) {
     alertList.remove(alert)
    }
}