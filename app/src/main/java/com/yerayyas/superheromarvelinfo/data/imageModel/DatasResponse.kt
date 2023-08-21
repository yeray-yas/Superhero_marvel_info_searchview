package com.yerayyas.superheromarvelinfo.data.imageModel


import com.google.gson.annotations.SerializedName

data class DatasResponse(
    @SerializedName("results")
    val results: List<Result>
)