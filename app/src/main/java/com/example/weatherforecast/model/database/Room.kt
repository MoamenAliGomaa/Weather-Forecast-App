package com.example.weatherforecast.model.database

import android.content.Context
import androidx.room.*
import com.example.weatherforecast.model.DataConverter
import com.example.weatherforecast.model.Pojos.Alert
import com.example.weatherforecast.model.Pojos.Welcome
import kotlinx.coroutines.flow.Flow

@Dao
interface AlertDao {
    @Query("SELECT * From Alert")
    fun getAlerts(): Flow<List<Alert>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAlert(alert: Alert): Long

    @Delete
    suspend fun deleteAlert(alert: Alert)
}

@Dao
interface WeatherDao {
    @Query("SELECT * FROM Welcome where isCurrent= true")
    fun getCurrentWeather(): List<Welcome>?

    @Query("SELECT * FROM Welcome where isFavorite= true")
    fun getFavoriteWeathers(): Flow<List<Welcome>?>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertWeather(welcome: Welcome): Long

    @Delete
    suspend fun deleteFavorite(welcome: Welcome):Int
//
//    @Query("UPDATE WelcomeCurrent SET lat = :lat ,lon = :lon,timezone = :timezone,timezone_offset= :timezone_offset,current = :current,hourly = :hourly ,daily = :daily ,isFavorite = :isFavorite WHERE isCurrent= true")
//    suspend fun updateCurrent( lat: Double,
//                               lon: Double,
//                             timezone: String,
//                              timezone_offset: Long,
//                             current: Current,
//                             hourly: List<Current>,
//                              daily: List<Daily>,
//                             isFavorite:Boolean?)

}

@Database(entities = arrayOf(Welcome::class,Alert::class), version = 13)
@TypeConverters(DataConverter::class)
abstract class WeatherDataBse : RoomDatabase() {
    abstract fun getWeatherDao(): WeatherDao
    abstract fun alertDao():AlertDao
    companion object {
        @Volatile
        private var INSTANCE: WeatherDataBse? = null
        fun getInstance(ctx: Context): WeatherDataBse {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    ctx.applicationContext, WeatherDataBse::class.java, "weather_database"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
// return instance
                instance
            }
        }
    }
}