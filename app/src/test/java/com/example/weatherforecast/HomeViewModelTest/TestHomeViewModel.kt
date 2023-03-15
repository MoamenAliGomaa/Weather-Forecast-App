package com.example.weatherforecast.HomeViewModelTest



import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.weatherforecast.DataSource.FakeLocalDataSource
import com.example.weatherforecast.DataSource.FakeRemoteDataSource
import com.example.weatherforecast.DataSource.FakeTestRepositary
import com.example.weatherforecast.MainCoroutineRule
import com.example.weatherforecast.model.IRepository
import com.example.weatherforecast.model.Network.IRemoteDataSource
import com.example.weatherforecast.model.Pojos.Alert
import com.example.weatherforecast.model.Pojos.ApiState
import com.example.weatherforecast.model.Pojos.Current
import com.example.weatherforecast.model.Pojos.Welcome
import com.example.weatherforecast.model.database.ILocalDataSource
import com.example.weatherforecast.ui.home.HomeViewModel

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.hamcrest.CoreMatchers.*
import org.hamcrest.MatcherAssert
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.core.IsNull
import org.junit.Before
import org.junit.Rule
import org.junit.Test


/**
 * Unit tests for the implementation of [TasksViewModel]
 */
@ExperimentalCoroutinesApi
class TasksViewModelTest {

    // Subject under test
    private lateinit var homeViewModel: HomeViewModel

    // Use a fake repository to be injected into the viewmodel
    private lateinit var repository: FakeTestRepositary

    // Executes each task synchronously using Architecture Components.
    @ExperimentalCoroutinesApi
    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()
    lateinit var localDataSource: ILocalDataSource
    lateinit var remoteDataSource: IRemoteDataSource
    lateinit var  welcomeList: MutableList<Welcome>
    lateinit var alertList:MutableList<Alert>
    lateinit var current1: Welcome
    @Before
    fun setupViewModel() {
        var current= Current(1355645,25,2552,255.00,5221.0,21522,2552,55225.0,252254.0,
            1420225,2654552,25225.0,2525,2145.00, listOf(),null,null)
        current1= Welcome(30.0,25.0,"152655",165552455,current, listOf(current),null,true,
            null,null)
        var data2= Welcome(30.0,25.0,"152655",165552455,current, listOf(current),null,true,
            null,null)
        var data3= Welcome(30.0,25.0,"152655",165552455,current, listOf(current),null,true,
            null,null)
        var data4= Welcome(30.0,25.0,"152655",165552455,current, listOf(current),null,true,
            null,null)
        val data = Alert(12323,13652,30.25,30.25,"Ismailia")
        val data5 =  Alert(12323,13652,30.25,30.25,"cairo")
        val data6 =  Alert(12323,13652,30.25,30.25,"suez")
        val data7 =  Alert(12323,13652,30.25,30.25,"port said")
        welcomeList=listOf(data2,data3,data4) as MutableList<Welcome>
        alertList=listOf(data,data5,data6,data7)as MutableList<Alert>

        localDataSource= FakeLocalDataSource(alertList,
           welcomeList ,
           current1
        )
        remoteDataSource= FakeRemoteDataSource(current1)
        repository = FakeTestRepositary(localDataSource,remoteDataSource)
        homeViewModel = HomeViewModel(repository)
    }

    @Test
    fun get_current_weather_return_welcome()= runBlocking {
    //given
        var lat=30.0
        var lon=25.0
        var data:Welcome = current1
        //when
        homeViewModel.getCurrentWeather(current1.lat.toString(),current1.lon.toString())
        var result=homeViewModel.welcomeCurrentWeather.first()

        when (result) {
            is ApiState.Loading -> {

            }
            is ApiState.Success -> {

                data = result.data
            }
            is ApiState.Fail -> {

            }
        }
        //Then
         assertThat(data, IsNull.notNullValue())

    }



}
