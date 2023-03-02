package com.example.weatherforecast.model.Pojos

sealed class  ApiState {
    class Success(var data : Welcome):ApiState()
    class Fail(val msg : Throwable):ApiState()
    object Loading :ApiState()


}