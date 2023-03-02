package com.example.weatherforecast.ui.dashboard

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.weatherforecast.model.Pojos.Settings
import com.example.weatherforecast.model.Repository

class SettingsViewModel(var context: Context) : ViewModel() {
    private var repository: Repository
    init {
        repository=Repository.getInstance(context)
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
class SettingsViewModelFactory(val context: Context): ViewModelProvider.Factory{
    override fun <T : ViewModel> create(modelClass: Class<T>) : T{
        return if (modelClass.isAssignableFrom(SettingsViewModel::class.java))
        {
            SettingsViewModel(context) as T
        }
        else{
            throw java.lang.IllegalArgumentException("View modle class not found")
        }
    }
}