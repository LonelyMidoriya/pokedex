package com.example.pokedex.database

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.pokedex.data.models.PokemonListEntry

@Dao
interface EntriesDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(entries: List<PokemonListEntry>)

    @Query("Select * From entries")
    fun getEntries(): PagingSource<Int, PokemonListEntry>

    @Query("Delete From entries")
    suspend fun clearAllEntries()
}