package com.example.weatherforecast.TestReposatiry

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.kotlinproducts.view.RetrofitHelper
import com.example.kotlinproducts.view.SimpleService
import com.example.weatherforecast.DataSource.FakeLocalDataSource
import com.example.weatherforecast.DataSource.FakeRemoteDataSource
import com.example.weatherforecast.model.IRepository
import com.example.weatherforecast.model.Network.IRemoteDataSource
import com.example.weatherforecast.model.Network.RemoteDataSource
import com.example.weatherforecast.model.Pojos.Alert
import com.example.weatherforecast.model.Pojos.Current
import com.example.weatherforecast.model.Pojos.Welcome
import com.example.weatherforecast.model.Repository
import com.example.weatherforecast.model.database.ILocalDataSource
import com.example.weatherforecast.model.database.LocalDataSource
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.hamcrest.MatcherAssert
import org.hamcrest.Matchers
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class ReposatiryTest {


    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    lateinit var repo : IRepository
    lateinit var localDataSource: ILocalDataSource
    lateinit var remoteDataSource: IRemoteDataSource


    @Before
    fun setUp() {
            var current= Current(1355645,25,2552,255.00,5221.0,21522,2552,55225.0,252254.0,
        1420225,2654552,25225.0,2525,2145.00, listOf(),null,null)
    var data1= Welcome(30.0,25.0,"152655",165552455,current, listOf(current),null,true,
        null,null)
    var data2=Welcome(30.0,25.0,"152655",165552455,current, listOf(current),null,true,
        null,null)
    var data3=Welcome(30.0,25.0,"152655",165552455,current, listOf(current),null,true,
        null,null)
    var data4=Welcome(30.0,25.0,"152655",165552455,current, listOf(current),null,true,
        null,null)
        val data = Alert(12323,13652,30.25,30.25,"Ismailia")
        val data5 =  Alert(12323,13652,30.25,30.25,"cairo")
        val data6 =  Alert(12323,13652,30.25,30.25,"suez")
        val data7 =  Alert(12323,13652,30.25,30.25,"port said")
        localDataSource=FakeLocalDataSource(listOf(data,data5,data6,data7)as MutableList<Alert>,
            listOf(data2,data3,data4) as MutableList<Welcome>,
            data1
            )
        remoteDataSource=FakeRemoteDataSource(data1)
        repo = Repository.getInstance(localDataSource,remoteDataSource)
    }

    @After
    fun tearDown() {
    }
    @Test
    fun get_current_weather_return_welcome()= runBlocking{
        //given
        var latitude=30.0
        var lon=25.0
        //when
        var response= repo.getCurrentWeather(latitude.toString(),lon.toString()).first()
        //then

        MatcherAssert.assertThat(response.lat, Matchers.`is`(latitude))
        MatcherAssert.assertThat(response.lon, Matchers.notNullValue())
    }
    @Test
    fun get_current_alert_return_alert()= runBlocking{
        //given
        var latitude=30.25
        var lon=30.25
        //when
        var response= repo.getAlert(1).first()
        //then

        MatcherAssert.assertThat(response.lat, Matchers.`is`(latitude))
        MatcherAssert.assertThat(response.lon,Matchers.`is`(lon))
    }

}