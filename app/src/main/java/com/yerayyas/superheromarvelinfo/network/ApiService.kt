package com.yerayyas.superheromarvelinfo.network

import com.yerayyas.superheromarvelinfo.data.model.ImageDatasResult
import com.yerayyas.superheromarvelinfo.data.model.SuperheroDataResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {

    @GET("/v1/public/characters")
    suspend fun getSuperheroByName(
        @Query("nameStartsWith") nameStartsWith: String,
        @Query("apikey") apiKey: String,
        @Query("hash") hash: String,
        @Query("ts") ts: Long
    ): Response<SuperheroDataResponse>

    @GET("/v1/public/characters")
    suspend fun getSuperheroes(
        @Query("offset") offset: Int? = 0,
        @Query("apikey") apiKey: String,
        @Query("hash") hash: String,
        @Query("ts") ts: Long
    ): Response<SuperheroDataResponse>

    @GET("/v1/public/characters/{characterId}")
    suspend fun getSuperheroesDetail(
        @Path("characterId") characterId: Int,
        @Query("apikey") apiKey: String,
        @Query("hash") hash: String,
        @Query("ts") ts: Long
    ): Response<ImageDatasResult>
}