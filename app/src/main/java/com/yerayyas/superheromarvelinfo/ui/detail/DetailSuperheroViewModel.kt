package com.yerayyas.superheromarvelinfo.ui.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yerayyas.superheromarvelinfo.network.ApiService
import com.yerayyas.superheromarvelinfo.data.model.ImageDatasResult
import com.yerayyas.superheromarvelinfo.network.ApiManager
import com.yerayyas.superheromarvelinfo.util.Constants.API_KEY
import com.yerayyas.superheromarvelinfo.util.Constants.HASH
import com.yerayyas.superheromarvelinfo.util.Constants.TS
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import retrofit2.Retrofit

class DetailSuperheroViewModel : ViewModel() {
    private val _superheroInfo = MutableStateFlow<ImageDatasResult?>(null)
    val superheroInfo: StateFlow<ImageDatasResult?> = _superheroInfo

    private val _errorState = MutableStateFlow<Boolean>(false)
    val errorState: StateFlow<Boolean> = _errorState

    private val retrofit: Retrofit = ApiManager.retrofit

    fun getSuperheroInfo(id: Int) {


        _errorState.value = false

        viewModelScope.launch {
            try {
                val superheroDetail = retrofit
                    .create(ApiService::class.java)
                    .getSuperheroesDetail(id, API_KEY, HASH, TS)

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
}
