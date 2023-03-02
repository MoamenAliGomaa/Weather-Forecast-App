package com.example.weatherforecast.ui.map

import android.content.Context
import androidx.lifecycle.*
import com.example.weatherforecast.model.Pojos.Settings
import com.example.weatherforecast.model.Repository
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect


private const val TAG = "HomeViewModel"

class MapsViewModel(var context: Context) : ViewModel() {
    private var repository: Repository


    init {
        repository = Repository.getInstance(context)
    }
    fun getSettings():Settings?{
        return repository.getSettings()
    }
    fun saveSettings(settings: Settings?){
        if (settings != null) {
            repository.saveSettings(settings)
        }
    }
    suspend fun insertFavorite(lat:Double,lon:Double){
        repository.getCurrentWeather(lat=lat.toString(),lon=lon.toString()).catch {  }.collect{
            it.isFavorite=true
            repository.insertFavoriteWeather(it)
        }
    }

}


class MapsViewModelFactory(val context: Context) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(MapsViewModel::class.java)) {
            MapsViewModel(context) as T
        } else {
            throw java.lang.IllegalArgumentException("View modle class not found")
        }
    }
}