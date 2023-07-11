package com.example.windy.favoritescreen.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.windy.model.RepositoryInterface

class FavoriteViewModelFactory (private val repository: RepositoryInterface) : ViewModelProvider.Factory{
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(FavoriteViewModel::class.java)) {
            FavoriteViewModel(repository) as T
        } else {
            throw java.lang.IllegalArgumentException("ViewModel Class not found")
        }
    }
}