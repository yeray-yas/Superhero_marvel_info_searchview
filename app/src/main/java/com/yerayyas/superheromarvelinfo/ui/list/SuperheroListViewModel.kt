package com.yerayyas.superheromarvelinfo.ui.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.yerayyas.superheromarvelinfo.data.SuperheroPagingSource
import com.yerayyas.superheromarvelinfo.data.model.SuperheroItemResponse
import com.yerayyas.superheromarvelinfo.data.repository.SuperheroRepository
import com.yerayyas.superheromarvelinfo.util.ApiCallException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.launch

enum class SuperheroListState {
    LOADING,
    LOADED,
    ERROR
}

class SuperheroListViewModel(private val repository: SuperheroRepository) : ViewModel() {

    private val _loadingState = MutableStateFlow(SuperheroListState.LOADING)
    val loadingState: StateFlow<SuperheroListState> = _loadingState

    private val _superheroesState = MutableStateFlow<PagingData<SuperheroItemResponse>>(PagingData.empty())
    val superheroesState: StateFlow<PagingData<SuperheroItemResponse>> = _superheroesState

    private val queryFlow = MutableStateFlow("")

    val superheroesFlow: Flow<PagingData<SuperheroItemResponse>> = queryFlow.flatMapLatest { query ->
        repository.getSuperheroesByNamePaged(query)
    }

    fun searchByName(query: String) {
        queryFlow.value = query
    }

    fun refreshData() {
        viewModelScope.launch {
            _loadingState.value = SuperheroListState.LOADING
            try {
                val pagingData = repository.refreshSuperheroes()
                _superheroesState.emitAll(pagingData)
                _loadingState.value = SuperheroListState.LOADED
            } catch (e: ApiCallException) {
                _loadingState.value = SuperheroListState.ERROR
            }
        }
    }
}

