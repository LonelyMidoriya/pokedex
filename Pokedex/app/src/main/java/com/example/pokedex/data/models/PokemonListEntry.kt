package com.example.pokedex.data.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "entries")
data class PokemonListEntry(
    @ColumnInfo(name = "name")
    val pokemonName: String,
    @ColumnInfo(name = "url")
    val imageUrl: String,
    @PrimaryKey(autoGenerate = false)
    var number: Int
)
