package com.example.weatherforecast.model.database

import android.content.Context
import androidx.room.*
import com.example.weatherforecast.model.DataConverter
import com.example.weatherforecast.model.Pojos.Alert
import com.example.weatherforecast.model.Pojos.Current
import com.example.weatherforecast.model.Pojos.Welcome
import kotlinx.coroutines.flow.Flow

@Dao
interface AlertDao {
    @Query("SELECT * From Alert")
    fun getAlerts(): Flow<List<Alert>>
    @Query("SELECT * From Alert Where startTime=:id")
    fun getAlert(id:Long): Flow<Alert>
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAlert(alert: Alert): Long

    @Delete
    suspend fun deleteAlert(alert: Alert)
}

@Dao
interface WeatherDao {

    //favorite
    @Query("SELECT * FROM Welcome where isFavorite= true")
   fun getFavoriteWeathers(): Flow<List<Welcome>?>
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWeather(welcome: Welcome): Long
    @Delete
    suspend fun deleteFavorite(welcome: Welcome):Int
    //current
    @Query("DELETE FROM Welcome where isFavorite= false")
    suspend fun deleteCurrent():Int
    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertCurrentWeather(welcome: Welcome): Long
    @Query("SELECT * FROM Welcome where isFavorite= false LIMIT 1")
    fun getCurrentWeathers(): Flow<Welcome>?
    @Transaction
    suspend fun insertOrUpdateCurrentWeather(welcome: Welcome)
    { val existingWeather=getCurrentWeathers()
        existingWeather?.let {
            deleteCurrent()
        }
        insertCurrentWeather(welcome)
    }
}

@Database(entities = arrayOf(Welcome::class,Alert::class), version = 24)
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