package com.example.weatherforecast.model.Network

import com.example.kotlinproducts.view.API
import com.example.weatherforecast.model.Pojos.Constants
import com.example.weatherforecast.model.Pojos.Welcome
import retrofit2.http.GET
import retrofit2.http.Query

class RemoteDataSource : IRemoteDataSource {


    override suspend fun getCurrentWeather(@Query("lat") lat: String?,
                                           @Query("lon") lon: String?,
                                           @Query("appid") appId:String,
                                           @Query("lang") lang:String,
                                           @Query("units") units:String ): Welcome{
        return API.retrofitService.getCurrentWeather(lat,lon,appId,lang,units)
    }
}