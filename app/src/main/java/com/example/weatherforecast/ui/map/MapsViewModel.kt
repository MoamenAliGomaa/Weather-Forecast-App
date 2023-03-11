package com.example.weatherforecast.ui.map

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.*
import com.example.weatherforecast.model.Pojos.AlertSettings
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
        repository.getCurrentWeather(lat=lat.toString(),lon=lon.toString()).catch {e->Toast.makeText(context,"Failed to fetch data $e",Toast.LENGTH_SHORT).show()
        }.collect{
            it.isFavorite=true
            repository.insertWeather(it)
        }
    }
    fun saveAlertSettings(alertSettings: AlertSettings){
        repository.saveAlertSettings(alertSettings)
    }
    fun getAlertSettings(): AlertSettings?{
        return repository.getAlertSettings()
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