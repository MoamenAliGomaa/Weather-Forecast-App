package com.example.weatherforecast.ui.map

import android.app.Application
import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.*
import com.example.weatherforecast.model.IRepository
import com.example.weatherforecast.model.Pojos.AlertSettings
import com.example.weatherforecast.model.Pojos.Settings
import com.example.weatherforecast.model.Repository
import com.example.weatherforecast.model.Utils
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect


private const val TAG = "HomeViewModel"

class MapsViewModel(var repository: IRepository,var context: Context) : ViewModel(){

    fun getSettings():Settings?{
        return repository.getSettings()
    }
    fun saveSettings(settings: Settings?){
        if (settings != null) {
            repository.saveSettings(settings)
        }
    }
    suspend fun insertFavorite(lat:Double,lon:Double){
        repository.getCurrentWeather(lat=lat.toString(),lon=lon.toString()).catch {e->
        }.collect{
            Log.e(TAG, "insertFavorite:  "+ it )
            it.countryName=Utils.getAddressEnglish(context,it.lat,it.lon)
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


class MapsViewModelFactory(val repository: Repository,val context: Context) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(MapsViewModel::class.java)) {
            MapsViewModel(repository,context) as T
        } else {
            throw java.lang.IllegalArgumentException("View modle class not found")
        }
    }
}