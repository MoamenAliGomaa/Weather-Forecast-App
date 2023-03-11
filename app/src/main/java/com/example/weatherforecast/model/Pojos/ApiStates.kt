package com.example.weatherforecast.model.Pojos

sealed class  ApiState {
    class Success(var data : Welcome):ApiState()
    class Fail(val msg : Throwable):ApiState()
    object Loading :ApiState()
}
sealed class  LocalDataState {
    class Success(var data: List<Welcome>?):LocalDataState()
    class Fail(val msg : Throwable):LocalDataState()
    object Loading :LocalDataState()
}
sealed class  LocalDataStateAlerts {
    class Success(var data: List<Alert>?):LocalDataStateAlerts()
    class Fail(val msg : Throwable):LocalDataStateAlerts()
    object Loading :LocalDataStateAlerts()
}