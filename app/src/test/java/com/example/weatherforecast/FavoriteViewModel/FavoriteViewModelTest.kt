package com.example.weatherforecast.FavoriteViewModel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.weatherforecast.DataSource.FakeLocalDataSource
import com.example.weatherforecast.DataSource.FakeRemoteDataSource
import com.example.weatherforecast.DataSource.FakeTestRepositary
import com.example.weatherforecast.MainCoroutineRule
import com.example.weatherforecast.model.IRepository
import com.example.weatherforecast.model.Network.IRemoteDataSource
import com.example.weatherforecast.model.Pojos.*
import com.example.weatherforecast.model.database.ILocalDataSource
import com.example.weatherforecast.model.database.LocalDataSource
import com.example.weatherforecast.ui.favorites.FavoritesViewModel
import com.example.weatherforecast.ui.home.HomeViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.hamcrest.MatcherAssert
import org.hamcrest.core.IsNull
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class FavoriteViewModelTest {
    // Subject under test
    private lateinit var favViewModel: FavoritesViewModel

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
        favViewModel = FavoritesViewModel(repository)
    }
    @Test
    fun get_favorite_retur_list_favorites()= runBlocking{
        //given
        var favList=welcomeList
        //when
        favViewModel.getFavoriteWeathers()
        var result=favViewModel.welcomeFavoriteWeather.value
        when (result) {
            is LocalDataState.Loading -> {

            }
            is LocalDataState.Success -> {

                favList = result.data as MutableList<Welcome>
            }
            is LocalDataState.Fail -> {

            }

    }
        //Then
        MatcherAssert.assertThat(favList, IsNull.notNullValue())
}
}