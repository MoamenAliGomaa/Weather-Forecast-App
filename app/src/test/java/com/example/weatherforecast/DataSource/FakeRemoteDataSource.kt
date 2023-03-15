package com.example.weatherforecast.DataSource

import com.example.weatherforecast.model.Network.IRemoteDataSource
import com.example.weatherforecast.model.Pojos.Welcome

class FakeRemoteDataSource(private var welcome: Welcome):IRemoteDataSource {
    override suspend fun getCurrentWeather(
        lat: String?,
        lon: String?,
        appId: String,
        lang: String,
        units: String
    ): Welcome {
       return welcome
    }
}