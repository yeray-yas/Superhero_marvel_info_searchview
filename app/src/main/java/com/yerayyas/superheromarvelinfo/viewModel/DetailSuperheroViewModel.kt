package com.yerayyas.superheromarvelinfo.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yerayyas.superheromarvelinfo.ApiService
import com.yerayyas.superheromarvelinfo.data.imageModel.ImageDatasResult
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class DetailSuperheroViewModel : ViewModel() {
    private val _superheroInfo = MutableStateFlow<ImageDatasResult?>(null)
    val superheroInfo: StateFlow<ImageDatasResult?> = _superheroInfo

    private val _errorState = MutableStateFlow<Boolean>(false)
    val errorState: StateFlow<Boolean> = _errorState

    fun getSuperheroInfo(id: Int) {
        val apiKey = "3de6bbd5de0a40038da2c8fe677fb23b"
        val hash = "56feb160b3d944895040bec40ead241b"
        val ts = 1

        _errorState.value = false

        viewModelScope.launch {
            try {
                val superheroDetail = getRetrofit()
                    .create(ApiService::class.java)
                    .getSuperheroesDetail(id, apiKey, hash, ts)

                val response = superheroDetail.body()
                response?.let {
                    _superheroInfo.value = it
                } ?: run {
                    _errorState.value = true
                }
            } catch (e: Exception) {
                _errorState.value = true
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
