package com.yerayyas.superheromarvelinfo.ui.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yerayyas.superheromarvelinfo.data.model.SuperheroItemResponse
import com.yerayyas.superheromarvelinfo.data.repository.SuperheroRepository
import com.yerayyas.superheromarvelinfo.util.ApiCallException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

enum class SuperheroListState {
    LOADING,
    LOADED,
    ERROR
}

class SuperheroListViewModel(private val repository: SuperheroRepository) : ViewModel() {

    private val _superheroesState = MutableStateFlow<List<SuperheroItemResponse>>(emptyList())
    val superheroesState: StateFlow<List<SuperheroItemResponse>> = _superheroesState

    private val _loadingState = MutableStateFlow(SuperheroListState.LOADING)
    val loadingState: StateFlow<SuperheroListState> = _loadingState

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery

    init {
        viewModelScope.launch {
            try {
                val superheroes = repository.getSuperheroes()
                _superheroesState.emit(superheroes)
                _loadingState.emit(SuperheroListState.LOADED)
            } catch (e: ApiCallException) {
                _loadingState.emit(SuperheroListState.ERROR)
            }
        }

        viewModelScope.launch {
            _searchQuery.collect { query ->
                performSearch(query)
            }
        }
    }

    private fun performSearch(query: String) {
        viewModelScope.launch {
            _loadingState.emit(SuperheroListState.LOADING)
            try {
                val superheroes = if (query.isBlank()) {
                    repository.getSuperheroes()
                } else {
                    repository.searchSuperheroesByName(query)
                }
                _superheroesState.emit(superheroes)
                _loadingState.emit(SuperheroListState.LOADED)
            } catch (e: ApiCallException) {
                _loadingState.emit(SuperheroListState.ERROR)
            }
        }
    }

    fun updateSearchQuery(query: String) {
        _searchQuery.value = query
    }
}
