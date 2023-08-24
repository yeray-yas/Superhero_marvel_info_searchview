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

    var searchQuery: String = ""
    private var superheroes: List<SuperheroItemResponse> = emptyList()

    fun searchByName(query: String) {
        viewModelScope.launch {
            _loadingState.value = SuperheroListState.LOADING
            try {
                val superheroes = if (query.isBlank()) {
                    repository.getSuperheroes()
                } else {
                    repository.searchSuperheroesByName(query)
                }
                val updatedSuperheroes = superheroes // Guardar el resultado en una variable local
                this@SuperheroListViewModel.superheroes = updatedSuperheroes // Actualizar la lista de superheroes directamente
                _superheroesState.emit(updatedSuperheroes)
                _loadingState.value = SuperheroListState.LOADED
            } catch (e: ApiCallException) {
                _loadingState.value = SuperheroListState.ERROR
            }
        }
    }

    fun loadAllSuperheroes() {
        viewModelScope.launch {
            _loadingState.value = SuperheroListState.LOADING
            try {
                val superheroes = repository.getSuperheroes()
                _superheroesState.emit(superheroes)
                _loadingState.value = SuperheroListState.LOADED
            } catch (e: ApiCallException) {
                _loadingState.value = SuperheroListState.ERROR
            }
        }
    }
}
