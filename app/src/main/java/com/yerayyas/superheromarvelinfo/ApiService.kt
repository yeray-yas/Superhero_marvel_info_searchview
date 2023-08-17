package com.yerayyas.superheromarvelinfo

import com.yerayyas.superheromarvelinfo.data.model.SuperheroDataResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {

    @GET("/v1/public/characters")
    suspend fun getSuperheroByName(
        @Query("nameStartsWith") nameStartsWith: String,
        //@Query("name") superheroName: String,
        @Query("apikey") apiKey: String,
        @Query("hash") hash: String,
        @Query("ts") ts: Int
    ): Response<SuperheroDataResponse>

    @GET("/v1/public/characters")
    suspend fun getSuperheroes(
        @Query("apikey") apiKey: String,
        @Query("hash") hash: String,
        @Query("ts") ts: Int
    ): Response<SuperheroDataResponse>
}