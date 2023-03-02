package com.example.weatherforecast.model.database

import android.content.Context
import androidx.room.*
import com.example.weatherforecast.model.DataConverter
import com.example.weatherforecast.model.Pojos.Welcome
import kotlinx.coroutines.flow.Flow


@Dao
interface WeatherDao{
    @Query("SELECT * FROM Welcome where isCurrent= true")
     fun getCurrentWeather() :Flow<Welcome>
    @Query("SELECT * FROM Welcome where isFavorite= true")
     fun getFavoriteWeathers() :Flow<List<Welcome>>
    @Insert(onConflict= OnConflictStrategy.REPLACE)
    suspend fun insertCurrentWeather( welcome: Welcome):Long
    @Insert(onConflict= OnConflictStrategy.REPLACE)
    suspend fun insertFavoriteWeather( welcome: Welcome):Long
    @Delete
    suspend fun deleteFavorite(welcome: Welcome)
}
@Database(entities = arrayOf(Welcome::class), version = 1 )
@TypeConverters(DataConverter::class)
abstract class WeatherDataBse : RoomDatabase() {
    abstract fun getWeatherDao(): WeatherDao

    companion object{
        @Volatile
        private var INSTANCE: WeatherDataBse? = null
        fun getInstance (ctx: Context): WeatherDataBse{
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    ctx.applicationContext, WeatherDataBse::class.java, "weather_database")
                    .build()
                INSTANCE = instance
// return instance
                instance }
        }
    }
}