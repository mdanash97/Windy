package com.example.windy.mapactivity.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.windy.favoritescreen.viewmodel.FavoriteViewModel
import com.example.windy.model.RepositoryInterface

class MapViewModelFactory(private val repository: RepositoryInterface) : ViewModelProvider.Factory{
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(MapViewModel::class.java)) {
            MapViewModel(repository) as T
        } else {
            throw java.lang.IllegalArgumentException("ViewModel Class not found")
        }
    }
}