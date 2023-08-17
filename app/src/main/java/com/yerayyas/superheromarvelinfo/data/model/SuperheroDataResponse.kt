package com.yerayyas.superheromarvelinfo.data.model

import com.google.gson.annotations.SerializedName

data class SuperheroDataResponse(
    @SerializedName("code")
    val code: Int,
    @SerializedName("data")
    val `data`: Data,
    @SerializedName("status")
    val status: String
)
