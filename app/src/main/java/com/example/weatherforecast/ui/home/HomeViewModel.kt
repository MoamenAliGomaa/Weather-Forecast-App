package com.example.weatherforecast.ui.home

import android.content.Context
import android.location.Location
import androidx.lifecycle.*

import com.example.weatherforecast.model.Pojos.Welcome
import com.example.weatherforecast.model.Repository
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

private const val TAG = "HomeViewModel"
class HomeViewModel(var context: Context) : ViewModel() {
    private var repository: Repository
    private var _welcomeCurrentWeather:MutableLiveData<Welcome> = MutableLiveData()
    val welcomeCurrentWeather: LiveData<Welcome> = _welcomeCurrentWeather



    init {
        repository=Repository.getInstance(context)
//        getCurrentWeather(coord?.value?.lat.toString(),coord?.value?.lon.toString())
    }
    fun getCurrentWeather(lat: String?, lon:String?){
        viewModelScope.launch(Dispatchers.IO) {
           _welcomeCurrentWeather.postValue(repository.getCurrentWeather(lat =lat, lon =lon))
        }

    }

}



class HomeViewModelFactory(val context: Context): ViewModelProvider.Factory{
    override fun <T : ViewModel> create(modelClass: Class<T>) : T{
        return if (modelClass.isAssignableFrom(HomeViewModel::class.java))
        {
            HomeViewModel(context) as T
        }
        else{
            throw java.lang.IllegalArgumentException("View modle class not found")
        }
    }
}