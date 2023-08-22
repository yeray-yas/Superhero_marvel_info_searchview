package com.yerayyas.superheromarvelinfo.viewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yerayyas.superheromarvelinfo.ApiService
import com.yerayyas.superheromarvelinfo.data.model.SuperheroDataResponse
import com.yerayyas.superheromarvelinfo.data.model.SuperheroItemResponse
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

enum class SuperheroListState {
    LOADING,
    LOADED,
    ERROR
}

class SuperheroListViewModel : ViewModel() {


    private val retrofit: Retrofit = getRetrofit()

    private val _superheroesState = MutableStateFlow<List<SuperheroItemResponse>>(emptyList())
    val superheroesState: StateFlow<List<SuperheroItemResponse>> = _superheroesState

    private val _loadingState = MutableStateFlow(SuperheroListState.LOADING)
    val loadingState: StateFlow<SuperheroListState> = _loadingState


    private val apiKey = "3de6bbd5de0a40038da2c8fe677fb23b"
    private val hash = "56feb160b3d944895040bec40ead241b"
    private val ts = 1

    fun searchByName(query: String) {
        viewModelScope.launch {
            _loadingState.value = SuperheroListState.LOADING
            val response: SuperheroDataResponse? = if (query.isBlank()) {
                retrofit.create(ApiService::class.java).getSuperheroes(apiKey, hash, ts).body()
            } else {
                retrofit.create(ApiService::class.java)
                    .getSuperheroByName(query, apiKey, hash, ts).body()
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
                val myResponse = retrofit.create(ApiService::class.java).getSuperheroes(apiKey, hash, ts)

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

    private fun getRetrofit(): Retrofit {
        return Retrofit
            .Builder()
            .baseUrl("https://gateway.marvel.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
}
