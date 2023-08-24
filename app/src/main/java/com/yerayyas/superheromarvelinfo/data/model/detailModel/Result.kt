package com.yerayyas.superheromarvelinfo.data.model.detailModel


import com.google.gson.annotations.SerializedName

data class Result(
    @SerializedName("description")
    val description: String,
    @SerializedName("name")
    val name: String,
    @SerializedName("thumbnail")
    val thumbnail: Thumbnails
)