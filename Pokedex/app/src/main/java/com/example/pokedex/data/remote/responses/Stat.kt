package com.example.pokedex.data.remote.responses


import com.example.pokedex.data.remote.responses.StatX
import com.google.gson.annotations.SerializedName

data class Stat(
    @SerializedName("base_stat")
    val baseStat: Int,
    val effort: Int,
    val stat: StatX
)