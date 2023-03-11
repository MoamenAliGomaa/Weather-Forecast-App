package com.example.weatherforecast.ui.home

import android.content.Context
import androidx.lifecycle.*
import com.example.weatherforecast.model.Pojos.ApiState
import com.example.weatherforecast.model.Pojos.Constants
import com.example.weatherforecast.model.Pojos.Settings

import com.example.weatherforecast.model.Repository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

private const val TAG = "HomeViewModel"

class HomeViewModel(var context: Context) : ViewModel() {
    private var repository: Repository
    private var _welcomeCurrentWeather: MutableStateFlow<ApiState>
     var welcomeCurrentWeather: StateFlow<ApiState>

    init {
        repository = Repository.getInstance(context)
        _welcomeCurrentWeather= MutableStateFlow(ApiState.Loading)
        welcomeCurrentWeather= _welcomeCurrentWeather
    }

    fun getCurrentWeather(
        lat: String?,
        lon: String?,
        lang: String = Constants.LANG_EN,
        units: String = Constants.UNITS_DEFAULT
    )
    {

        viewModelScope.launch{

                repository.getCurrentWeather(
                    lat = lat,
                    lon = lon,
                    lang = lang,
                    units = units
                ).catch { e->_welcomeCurrentWeather.value=ApiState.Fail(e) }.collect{
                    apiWeather->
                    _welcomeCurrentWeather.value=ApiState.Success(apiWeather)

                }

        }
    }


    fun getSettings(): Settings? {
        return repository.getSettings()
    }

}

class HomeViewModelFactory(val context: Context) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
            HomeViewModel(context) as T
        }
        else {
            throw java.lang.IllegalArgumentException("View modle class not found")
        }
    }
}