package com.yerayyas.superheromarvelinfo.data.model


import com.google.gson.annotations.SerializedName

data class Thumbnails(
    @SerializedName("extension")
    val extension: String,
    @SerializedName("path")
    val path: String
)