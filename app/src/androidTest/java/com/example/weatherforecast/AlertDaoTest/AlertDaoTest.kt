package com.example.weatherforecast

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.example.weatherforecast.model.Pojos.Alert
import com.example.weatherforecast.model.database.AlertDao
import com.example.weatherforecast.model.database.WeatherDataBse

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.CoreMatchers

import org.hamcrest.MatcherAssert
import org.hamcrest.core.IsNull
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
@SmallTest
class AlertDAOTest {
    @get:Rule
    var instantTaskExecutorRule=InstantTaskExecutorRule()
    private lateinit var db: WeatherDataBse
    private lateinit var alertDao: AlertDao

    @Before
    fun setUp() {

        //initialize database
        db= Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            WeatherDataBse::class.java
        ).allowMainThreadQueries().build()
        alertDao= db.alertDao()
    }

    @After
    fun tearDown() {

        //close database
        db.close()

    }

    @Test
    fun allAlerts() = runBlockingTest{

        val data = Alert(12323,13652,30.25,30.25,"Ismailia")
        val data1 =  Alert(12323,13652,30.25,30.25,"cairo")
        val data2 =  Alert(12323,13652,30.25,30.25,"suez")
        val data3 =  Alert(12323,13652,30.25,30.25,"port said")

        alertDao.insertAlert(data)
        alertDao.insertAlert(data1)
        alertDao.insertAlert(data2)
        alertDao.insertAlert(data3)

        //When
        val result = alertDao.getAlerts()
            .first()

        //Then
        MatcherAssert.assertThat(result.size, CoreMatchers.`is`(4))
    }

    @Test
    fun insertFavourite_insertOneItem_returnTheItem() = runBlockingTest {
        //Given
        val data =  Alert(12323,13652,30.25,30.25,"Ismailia")

        //When
        alertDao.insertAlert(data)

        //Then
        val result= alertDao.getAlerts().first()
        MatcherAssert.assertThat(result[0], IsNull.notNullValue())

    }

    @Test
    fun insertAlert() {
    }

    @Test
    fun deleteAlert(){

    }
}