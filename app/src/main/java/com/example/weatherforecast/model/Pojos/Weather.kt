package com.example.weatherforecast.model.Pojos

import android.text.format.Time
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(primaryKeys = ["lat", "lon"])
data class Welcome (
    val lat: Double,
    val lon: Double,
    val timezone: String,
    val timezone_offset: Long,
    val current: Current,
    val hourly: List<Current>,
    val daily: List<Daily>,
    var isCurrent:Boolean?,
    var isFavorite:Boolean?,
    val minutely: List<Minutely>,
    val alerts: List<Alert>?=null
)
//@Entity(primaryKeys = ["lat", "lon"])
//data class WelcomeCurrent (
//    val lat: Double,
//    val lon: Double,
//    val timezone: String,
//    val timezone_offset: Long,
//    val current: Current,
//    val hourly: List<Current>,
//    val daily: List<Daily>,
//    var isCurrent:Boolean?,
//    var isFavorite:Boolean?,
//    val minutely: List<Minutely>,
//    val alerts: List<Alert>?=null
//)

data class Current (
    val dt: Long,
    val sunrise: Long? = null,
    val sunset: Long? = null,
    val temp: Double,
    val feels_like: Double,
    val pressure: Long,
    val humidity: Long,
    val dew_point: Double,
    val uvi: Double,
    val clouds: Long,
    val visibility: Long,
    val wind_speed: Double,
    val wind_deg: Long,
    val wind_gust: Double,
    val weather: List<Weather>,
    val pop: Double? = null,
    val rain: Rain? = null
)

data class Rain (
    val the1H: Double
)

data class Weather (

    val id: Long,
    val main: Main,
    val description: String,
    val icon: String
)

enum class Main {
    Clear,
    Clouds,
    Rain,
    Snow
}

data class Daily (
    val dt: Long,
    val sunrise: Long,
    val sunset: Long,
    val moonrise: Long,
    val moonset: Long,
    val moonPhase: Double,
    val temp: Temp,
    val feels_like: FeelsLike,
    val pressure: Long,
    val humidity: Long,
    val dewPoint: Double,
    val wind_speed: Double,
    val wind_deg: Long,
    val winddust: Double,
    val weather: List<Weather>,
    val clouds: Long,
    val pop: Double,
    val uvi: Double,
    val rain: Double? = null,
    val snow: Double? = null

)

data class FeelsLike (
    val day: Double,
    val night: Double,
    val eve: Double,
    val morn: Double

)

data class Temp (
    val day: Double,
    val min: Double,
    val max: Double,
    val night: Double,
    val eve: Double,
    val morn: Double

)

data class Minutely (
    val dt: Long,
    val precipitation:Long
)








enum class Description {
    BrokenClouds,
    ClearSky,
    FewClouds,
    LightSnow,
    OvercastClouds,
    ScatteredClouds,
    Snow
}

enum class Icon {
    The01D,
    The01N,
    The02N,
    The03N,
    The04D,
    The04N,
    The13D
}
@Entity(tableName = "Alert")
data class Alert(
    @PrimaryKey(autoGenerate = true)
    val id:Int?=null,
   var startTime: Long,
    var endTime:Long,
    var lat:Double,
    var lon:Double,
    var cityName:String?=null
    )

