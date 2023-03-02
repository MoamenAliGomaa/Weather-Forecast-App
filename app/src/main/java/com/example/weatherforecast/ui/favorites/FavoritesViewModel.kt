package com.example.weatherforecast.ui.favorites

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.weatherforecast.model.Pojos.ApiState
import com.example.weatherforecast.model.Pojos.Constants
import com.example.weatherforecast.model.Pojos.Settings
import com.example.weatherforecast.model.Repository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch


class FavoritesViewModel(var context: Context) : ViewModel() {
    private var repository: Repository


    init {
        repository = Repository.getInstance(context)

    }
    fun getFavoriteWeathers(){
        //todo get favorites from data base
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