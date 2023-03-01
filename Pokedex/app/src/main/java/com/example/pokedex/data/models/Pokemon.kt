package com.example.pokedex.data.models


data class Pokemon(
    val height: Int,
    val id: Int,
    val name: String,
    val types: List<Type>,
    val weight: Int
)