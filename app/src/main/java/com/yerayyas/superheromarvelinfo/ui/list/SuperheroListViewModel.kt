package com.yerayyas.superheromarvelinfo.ui.list

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yerayyas.superheromarvelinfo.network.ApiService
import com.yerayyas.superheromarvelinfo.data.model.SuperheroDataResponse
import com.yerayyas.superheromarvelinfo.data.model.SuperheroItemResponse
import com.yerayyas.superheromarvelinfo.network.ApiManager
import com.yerayyas.superheromarvelinfo.util.Constants.API_KEY
import com.yerayyas.superheromarvelinfo.util.Constants.HASH
import com.yerayyas.superheromarvelinfo.util.Constants.TS
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import retrofit2.Retrofit

enum class SuperheroListState {
    LOADING,
    LOADED,
    ERROR
}

class SuperheroListViewModel : ViewModel() {


    private val retrofit: Retrofit = ApiManager.retrofit

    private val _superheroesState = MutableStateFlow<List<SuperheroItemResponse>>(emptyList())
    val superheroesState: StateFlow<List<SuperheroItemResponse>> = _superheroesState

    private val _loadingState = MutableStateFlow(SuperheroListState.LOADING)
    val loadingState: StateFlow<SuperheroListState> = _loadingState

    fun searchByName(query: String) {
        viewModelScope.launch {
            _loadingState.value = SuperheroListState.LOADING
            val response: SuperheroDataResponse? = if (query.isBlank()) {
                retrofit.create(ApiService::class.java).getSuperheroes( API_KEY, HASH, TS).body()
            } else {
                retrofit.create(ApiService::class.java)
                    .getSuperheroByName(query, API_KEY, HASH, TS).body()
            }

            response?.let {
                _superheroesState.emit(it.data.superheroes)
            }

            _loadingState.value = SuperheroListState.LOADED
        }
    }

    fun loadAllSuperheroes() {

        viewModelScope.launch {
            _loadingState.value = SuperheroListState.LOADING
            try {
                val myResponse = retrofit.create(ApiService::class.java).getSuperheroes( API_KEY, HASH, TS)

                if (myResponse.isSuccessful) {
                    val response: SuperheroDataResponse? = myResponse.body()
                    response?.let {
                        _superheroesState.emit(it.data.superheroes)
                        _loadingState.value = SuperheroListState.LOADED
                    }
                } else {
                    Log.d("rrrr", "API call not successful")
                }

            } catch (e: Exception) {
                Log.e("rrrr", "API call error: ${e.message}")
            } finally {
                _loadingState.value = SuperheroListState.ERROR
            }
        }
    }
}
