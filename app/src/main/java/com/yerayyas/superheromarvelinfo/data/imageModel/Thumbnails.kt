package com.yerayyas.superheromarvelinfo.data.imageModel


import com.google.gson.annotations.SerializedName

data class Thumbnails(
    @SerializedName("extension")
    val extension: String,
    @SerializedName("path")
    val path: String
)