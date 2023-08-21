package com.yerayyas.superheromarvelinfo.data.model

import com.google.gson.annotations.SerializedName

data class SuperheroDetailResponse(
    @SerializedName("code")
    val code: Int,
    @SerializedName("data")
    val `data`: DataResponse,
    @SerializedName("status")
    val status: String
)

data class DataResponse(
    @SerializedName("results")
    val superheroes: List<SuperheroResultsResponse>,
)

data class SuperheroResultsResponse(
    @SerializedName("description")
    val description: String,
    @SerializedName("name")
    val name: String,
    @SerializedName("thumbnail")
    val thumbnail: Thumbnail
)
