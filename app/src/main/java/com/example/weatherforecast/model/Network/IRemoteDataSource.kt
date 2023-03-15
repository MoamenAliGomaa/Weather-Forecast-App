package com.example.weatherforecast.model.Network

import com.example.weatherforecast.model.Pojos.Constants
import com.example.weatherforecast.model.Pojos.Welcome
import retrofit2.http.Query

interface IRemoteDataSource {
    suspend fun getCurrentWeather(
        @Query("lat") lat: String?,
        @Query("lon") lon: String?,
        @Query("appid") appId: String = Constants.APPID,
        @Query("lang") lang: String,
        @Query("units") units: String
    ): Welcome
}