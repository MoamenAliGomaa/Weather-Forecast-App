package com.example.weatherforecast.ui.favorites

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.weatherforecast.model.Pojos.*
import com.example.weatherforecast.model.Repository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

private const val TAG = "FavoritesViewModel"
class FavoritesViewModel(var context: Context) : ViewModel() {
    private var repository: Repository
    private var _welcomeFavoriteWeather: MutableStateFlow<LocalDataState>
     var welcomeFavoriteWeather: StateFlow<LocalDataState>

    init {
        repository = Repository.getInstance(context)
        _welcomeFavoriteWeather= MutableStateFlow(LocalDataState.Loading)
        welcomeFavoriteWeather= _welcomeFavoriteWeather
    }
    fun getFavoriteWeathers(){
        viewModelScope.launch {
            repository.getFavoriteWeathers().catch {e->_welcomeFavoriteWeather.value=LocalDataState.Fail(e)  }.collectLatest {
                _welcomeFavoriteWeather.value=LocalDataState.Success(it)
                Log.i(TAG, "getFavoriteWeathers: "+it)
            }
        }
        }
    fun deleteFavorite(welcome: Welcome)
    {viewModelScope.launch{
        repository.deleteFavorite(welcome)
    }
    }

    }





class FavoritesViewModelFactory(val context: Context) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(FavoritesViewModel::class.java)) {
            FavoritesViewModel(context) as T
        } else {
            throw java.lang.IllegalArgumentException("View modle class not found")
        }
    }
}