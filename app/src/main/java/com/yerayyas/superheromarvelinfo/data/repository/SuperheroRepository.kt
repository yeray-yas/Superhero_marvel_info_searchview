package com.yerayyas.superheromarvelinfo.data.repository

import com.yerayyas.superheromarvelinfo.data.model.ImageDatasResult
import com.yerayyas.superheromarvelinfo.data.model.SuperheroItemResponse
import com.yerayyas.superheromarvelinfo.network.ApiService
import com.yerayyas.superheromarvelinfo.util.ApiCallException
import com.yerayyas.superheromarvelinfo.util.Constants.API_KEY
import com.yerayyas.superheromarvelinfo.util.Constants.HASH
import com.yerayyas.superheromarvelinfo.util.Constants.TS

class SuperheroRepository(private val apiService: ApiService) {

    suspend fun getSuperheroes(): List<SuperheroItemResponse> {
        val response = apiService.getSuperheroes(API_KEY, HASH, TS)
        if (response.isSuccessful) {
            return response.body()?.data?.superheroes ?: emptyList()
        } else {
            // Manejo de errores en caso de que la llamada no sea exitosa
            throw ApiCallException("Superheroes API call not successful")
        }
    }

    suspend fun getSuperheroDetail(id: Int): ImageDatasResult {
        val response = apiService.getSuperheroesDetail(id, API_KEY, HASH, TS)
        if (response.isSuccessful) {
            val responseBody = response.body()
            if (responseBody != null) {
                return responseBody
            } else {
                throw ApiCallException("Superhero details not found")
            }
        } else {
            throw ApiCallException("Superhero details API call not successful")
        }
    }

    suspend fun searchSuperheroesByName(query: String): List<SuperheroItemResponse> {
        val response = apiService.getSuperheroByName(query, API_KEY, HASH, TS)

        if (response.isSuccessful) {
            val superheroDataResponse = response.body()
            superheroDataResponse?.let {
                return it.data.superheroes
            }
        }

        throw ApiCallException("Error searching superheroes by name")
    }
}
