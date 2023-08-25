package com.yerayyas.superheromarvelinfo.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.PagingSource
import com.yerayyas.superheromarvelinfo.data.SuperheroPagingSource
import com.yerayyas.superheromarvelinfo.data.model.ImageDatasResult
import com.yerayyas.superheromarvelinfo.data.model.SuperheroItemResponse
import com.yerayyas.superheromarvelinfo.network.ApiService
import com.yerayyas.superheromarvelinfo.util.ApiCallException
import com.yerayyas.superheromarvelinfo.util.Constants.API_KEY
import com.yerayyas.superheromarvelinfo.util.Constants.HASH
import com.yerayyas.superheromarvelinfo.util.Constants.TS
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map

class SuperheroRepository(private val apiService: ApiService) {

    fun getSuperheroesByNamePaged(query: String): Flow<PagingData<SuperheroItemResponse>> {
        return Pager(
            config = PagingConfig(pageSize = SuperheroPagingSource.PAGE_SIZE),
            pagingSourceFactory = { SuperheroPagingSource(apiService, query) }
        ).flow
    }

    suspend fun refreshSuperheroes(): Flow<PagingData<SuperheroItemResponse>> {
        val pagingSourceFactory = { SuperheroPagingSource(apiService, "") }

        return Pager(
            config = PagingConfig(pageSize = SuperheroPagingSource.PAGE_SIZE),
            pagingSourceFactory = pagingSourceFactory
        ).flow
    }



//    suspend fun getSuperheroes(query): Flow<PagingData<SuperheroItemResponse>> {
//        return Pager(
//            config = PagingConfig(pageSize = SuperheroPagingSource.PAGE_SIZE),
//            pagingSourceFactory = { SuperheroPagingSource(apiService, query = query) }
//        ).flow
//    }

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
