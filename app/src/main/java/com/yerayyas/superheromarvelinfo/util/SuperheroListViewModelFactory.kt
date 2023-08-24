package com.yerayyas.superheromarvelinfo.util

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.yerayyas.superheromarvelinfo.data.repository.SuperheroRepository
import com.yerayyas.superheromarvelinfo.ui.list.SuperheroListViewModel

class SuperheroListViewModelFactory(private val repository: SuperheroRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SuperheroListViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return SuperheroListViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
