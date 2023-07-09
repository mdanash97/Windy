package com.example.windy.homescreen.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.windy.model.RepositoryInterface

class HomeViewModelFactory (private val repository: RepositoryInterface) : ViewModelProvider.Factory{
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
            HomeViewModel(repository) as T
        } else {
            throw java.lang.IllegalArgumentException("ViewModel Class not found")
        }
    }
}