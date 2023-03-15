package com.example.kotlinproducts.view


import com.example.kotlinproducts.view.RetrofitHelper.retrofit
import com.example.weatherforecast.model.Pojos.Constants
import com.example.weatherforecast.model.Pojos.Welcome
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

const val BASE_URL="https://api.openweathermap.org/data/2.5/"
interface SimpleService{
    @GET("onecall?")
    suspend fun getCurrentWeather(@Query("lat") lat: String?,
                                  @Query("lon") lon: String?,
                                  @Query("appid") appId:String=Constants.APPID,
                                  @Query("lang") lang:String,
                                  @Query("units") units:String ): Welcome
    @GET("onecall?")
    suspend fun getCurrentWeatherTCallBack(@Query("lat") lat: String?,
                                  @Query("lon") lon: String?,
                                  @Query("appid") appId:String=Constants.APPID,
                                  @Query("lang") lang:String,
                                  @Query("units") units:String ): Response<Welcome>

}
object RetrofitHelper {
    val retrofit = Retrofit.Builder()
        .addConverterFactory(GsonConverterFactory.create())
        .baseUrl(BASE_URL)
        .build()
}
object API {
    val retrofitService: SimpleService by lazy {
        retrofit.create(SimpleService::class.java)
    }
}
