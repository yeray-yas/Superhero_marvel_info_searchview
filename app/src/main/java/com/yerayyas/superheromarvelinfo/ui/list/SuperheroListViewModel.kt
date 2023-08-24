package com.yerayyas.superheromarvelinfo.ui.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yerayyas.superheromarvelinfo.data.model.SuperheroItemResponse
import com.yerayyas.superheromarvelinfo.data.repository.SuperheroRepository
import com.yerayyas.superheromarvelinfo.util.ApiCallException
import com.yerayyas.superheromarvelinfo.util.SuperheroListState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch



class SuperheroListViewModel(private val repository: SuperheroRepository) : ViewModel() {

    private val _superheroesState = MutableStateFlow<SuperheroListState>(SuperheroListState.Loading)
    val superheroesState: StateFlow<SuperheroListState> = _superheroesState

    private val _loadingState = MutableStateFlow(SuperheroListState.Loading)
    val loadingState: StateFlow<SuperheroListState> = _loadingState

    fun searchByName(query: String) {
        viewModelScope.launch {
            _superheroesState.value = SuperheroListState.Loading
            try {
                val superheroes = if (query.isBlank()) {
                    repository.getSuperheroes()
                } else {
                    repository.searchSuperheroesByName(query)
                }
                _superheroesState.value = SuperheroListState.Loaded(superheroes)
            } catch (e: ApiCallException) {
                _superheroesState.value = SuperheroListState.Error
            }
        }
    }

    fun loadAllSuperheroes() {
        viewModelScope.launch {
            _loadingState.value = SuperheroListState.Loading
            try {
                val superheroes = repository.getSuperheroes()
                _superheroesState.emit(superheroes)
                _loadingState.value = SuperheroListState.Loaded
            } catch (e: ApiCallException) {
                _loadingState.value = SuperheroListState.Error
            }
        }
    }


}
