package com.example.pokedex.data.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "pokemon")
data class DBPokemon(
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "id")
    val id: Int,
    @ColumnInfo(name = "height")
    val height: Int,
    @ColumnInfo(name = "name")
    val name: String,
    @ColumnInfo(name = "types")
    val types: String,
    @ColumnInfo(name = "weight")
    val weight: Int
)