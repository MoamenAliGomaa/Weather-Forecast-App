package com.example.weatherforecast.testRetrofit

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.kotlinproducts.view.API
import com.example.kotlinproducts.view.RetrofitHelper
import com.example.kotlinproducts.view.SimpleService
import com.example.weatherforecast.model.Pojos.Constants
import com.google.android.gms.common.api.Api
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.hamcrest.MatcherAssert
import org.hamcrest.Matchers.`is`
import org.hamcrest.Matchers.notNullValue
import org.hamcrest.core.IsNull
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
class ApiTest {

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    lateinit var apiObj : SimpleService


    @Before
    fun setUp() {
        apiObj = RetrofitHelper.retrofit.create(SimpleService::class.java)
    }

    @After
    fun tearDown() {
    }

    @Test
    fun getRoot_requestKey_Authorized() = runBlocking{
        //Given
        val longitude =30.0
        val latitude = 25.0
        val language="en"
        val unit = "metric"
        val apiKey=Constants.APPID

        //When
        val response= apiObj.getCurrentWeather(
            latitude.toString()
            , longitude.toString()
            , apiKey
            , language
            , unit

        )
        //Then
        MatcherAssert.assertThat(response.lat,`is` (latitude))
        MatcherAssert.assertThat(response.current, notNullValue())


    }
    @Test
    fun getRoot_requestNoKey_unAuthorized() = runBlocking{
        //Given
        val longitude =30.0
        val latitude = 25.0
        val language="en"
        val unit = "metric"
        val apiKey = "Constants.APPID"

        //When
        val response= apiObj.getCurrentWeatherTCallBack(
            latitude.toString()
            , longitude.toString()
            , apiKey
            , unit
            , language
        )

        //Then.
        MatcherAssert.assertThat(response.code(), `is` (401))
        MatcherAssert.assertThat(response.body(), IsNull.nullValue())



    }
}