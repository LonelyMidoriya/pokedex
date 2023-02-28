package com.example.pokedex.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.pokedex.data.models.PokemonListEntry
import com.example.pokedex.data.remote.responses.DBPokemon


@Database(
        entities = [PokemonListEntry::class, RemoteKeys::class, DBPokemon::class],
        version = 1,
    )
    abstract class PokemonDatabase: RoomDatabase() {
        abstract fun getEntriesDao(): EntriesDao
        abstract fun getRemoteKeysDao(): RemoteKeysDao
        abstract fun getPokemonDao(): PokemonDao
    }