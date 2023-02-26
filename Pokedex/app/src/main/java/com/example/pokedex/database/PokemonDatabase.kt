package com.example.pokedex.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.pokedex.data.models.PokemonListEntry


@Database(
        entities = [PokemonListEntry::class, RemoteKeys::class],
        version = 1,
    )
    abstract class PokemonDatabase: RoomDatabase() {
        abstract fun getEntriesDao(): EntriesDao
        abstract fun getRemoteKeysDao(): RemoteKeysDao
    }