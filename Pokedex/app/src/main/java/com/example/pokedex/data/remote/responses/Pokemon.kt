package com.example.pokedex.data.remote.responses


import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.pokedex.data.remote.responses.*
import com.google.gson.annotations.SerializedName


data class Pokemon(
    val height: Int,
    val id: Int,
    val name: String,
    val types: List<Type>,
    val weight: Int
)