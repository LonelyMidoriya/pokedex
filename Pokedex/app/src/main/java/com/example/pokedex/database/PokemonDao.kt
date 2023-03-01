package com.example.pokedex.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.pokedex.data.models.DBPokemon

@Dao
interface PokemonDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(pokemon: List<DBPokemon>)

    @Query("Select * From pokemon Where name = :name ")
    fun getPokemon(name: String): DBPokemon

    @Query("Delete From pokemon")
    suspend fun clearAllPokemons()
}