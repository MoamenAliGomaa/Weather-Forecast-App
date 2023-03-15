package com.example.weatherforecast.ui.home

import android.content.Context
import androidx.lifecycle.*
import com.example.weatherforecast.model.IRepository
import com.example.weatherforecast.model.Pojos.*

import com.example.weatherforecast.model.Repository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

private const val TAG = "HomeViewModel"

class HomeViewModel(var repository: IRepository) : ViewModel() {
//    private var repository: Repository
    private var _welcomeCurrentWeather: MutableStateFlow<ApiState>
     var welcomeCurrentWeather: StateFlow<ApiState>
    private var _welcomeCurrentWeatherLocal: MutableStateFlow<ApiState>
    var welcomeCurrentWeatherLocal: StateFlow<ApiState>

    init {
//        repository = Repository.getInstance(context)
        _welcomeCurrentWeather= MutableStateFlow(ApiState.Loading)
        welcomeCurrentWeather= _welcomeCurrentWeather
        _welcomeCurrentWeatherLocal=MutableStateFlow(ApiState.Loading)
        welcomeCurrentWeatherLocal=_welcomeCurrentWeatherLocal
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
                ).catch { e->_welcomeCurrentWeather.value=ApiState.Fail(e) }.collectLatest{
                    apiWeather->
                    _welcomeCurrentWeather.value=ApiState.Success(apiWeather)

                }

        }
    }


    fun getSettings(): Settings? {
        return repository.getSettings()
    }

    fun insertOrUpdateCurrent(welcome: Welcome)
        {   viewModelScope.launch{
            repository.insertOrUpdateCurrentWeather(welcome)
        }
        }
    fun getCurrentWeatherLocal(){
        viewModelScope.launch {
        repository.getCurrentWeatherDB()?.catch {
                e->_welcomeCurrentWeatherLocal.value=ApiState.Fail(e)
        }?.collectLatest {
            _welcomeCurrentWeatherLocal.value=ApiState.Success(it)
        }
    }
    }


}

class HomeViewModelFactory(val repository: Repository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
            HomeViewModel(repository) as T
        }
        else {
            throw java.lang.IllegalArgumentException("View modle class not found")
        }
    }
}