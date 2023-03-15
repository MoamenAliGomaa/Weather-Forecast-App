package com.example.weatherforecast.ui.dashboard

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.weatherforecast.model.IRepository
import com.example.weatherforecast.model.Pojos.Settings
import com.example.weatherforecast.model.Repository

class SettingsViewModel(var repository: IRepository) : ViewModel() {
    //private var repository: Repository
    init {
     //   repository=Repository.getInstance(context)
    }
    fun saveSettings(settings: Settings?){
        if (settings != null) {
            repository.saveSettings(settings)

        }
    }
    fun getSettings():Settings?{
        return repository.getSettings()
    }
}
class SettingsViewModelFactory(val repository: Repository): ViewModelProvider.Factory{
    override fun <T : ViewModel> create(modelClass: Class<T>) : T{
        return if (modelClass.isAssignableFrom(SettingsViewModel::class.java))
        {
            SettingsViewModel(repository) as T
        }
        else{
            throw java.lang.IllegalArgumentException("View modle class not found")
        }
    }
}