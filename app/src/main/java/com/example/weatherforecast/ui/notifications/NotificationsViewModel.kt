package com.example.weatherforecast.ui.notifications

import android.content.Context
import androidx.lifecycle.*
import com.example.weatherforecast.model.Pojos.Alert
import com.example.weatherforecast.model.Pojos.AlertSettings
import com.example.weatherforecast.model.Pojos.LocalDataState
import com.example.weatherforecast.model.Pojos.LocalDataStateAlerts
import com.example.weatherforecast.model.Repository
import com.example.weatherforecast.ui.home.HomeViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class NotificationsViewModel(context: Context) : ViewModel() {
    private var repository: Repository
    private var _alertFlow: MutableStateFlow<LocalDataStateAlerts>
    var alertFlow: StateFlow<LocalDataStateAlerts>

    init {
        repository = Repository.getInstance(context)
        _alertFlow= MutableStateFlow(LocalDataStateAlerts.Loading)
        alertFlow= _alertFlow
    }
    fun insertAlert(alert: Alert){
        viewModelScope.launch{
            repository.insertAlert(alert)
        }

    }
     fun deleteAlert(alert: Alert){
        viewModelScope.launch {
            repository.deleteAlert(alert)
        }
    }
    fun getAlerts(){
        viewModelScope.launch {
            repository.getAlerts().catch { e->_alertFlow.value=LocalDataStateAlerts.Fail(e)}.collectLatest {
                _alertFlow.value=LocalDataStateAlerts.Success(it)
            }
        }

    }
    fun getAlertSettings(): AlertSettings?{
        return repository.getAlertSettings()
    }
    fun saveAlertSettings(alertSettings: AlertSettings){
        repository.saveAlertSettings(alertSettings)
    }


}

class NotificationsViewModelFactory(val context: Context) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(NotificationsViewModel::class.java)) {
            NotificationsViewModel(context) as T
        } else {
            throw java.lang.IllegalArgumentException("View modle class not found")
        }
    }
}