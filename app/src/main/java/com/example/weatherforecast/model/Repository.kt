package com.example.weatherforecast.model

import android.content.Context
import com.example.kotlinproducts.view.API
import com.example.weatherforecast.model.Pojos.Constants
import com.example.weatherforecast.model.Pojos.Welcome

class Repository private constructor(var context: Context){
    companion object{
        @Volatile
        private var INSTANCE: Repository? = null
        fun getInstance (ctx: Context): Repository{
            return INSTANCE ?: synchronized(this) {
                val instance =Repository(ctx)
                INSTANCE = instance
// return instance
                instance }
        }
    }
    suspend fun getCurrentWeather(lat: String?, lon: String?, lang:String=Constants.LANG_EN, units:String= Constants.UNITS_DEFAULT):Welcome{
       return API.retrofitService.getCurrentWeather(lat = lat, lon =lon, lang =lang, units = units)
    }
}