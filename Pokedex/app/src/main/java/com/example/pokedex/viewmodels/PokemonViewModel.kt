package com.example.pokedex.viewmodels

import androidx.lifecycle.ViewModel
import com.example.pokedex.data.remote.responses.DBPokemon
import com.example.pokedex.data.remote.responses.Pokemon
import com.example.pokedex.database.PokemonDatabase
import com.example.pokedex.repository.PokemonRepository
import com.example.pokedex.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class PokemonViewModel @Inject constructor(
    private val database: PokemonDatabase,
    private val repository: PokemonRepository
) : ViewModel() {

    fun getPokemonInfo(pokemonName: String): DBPokemon {
           return database.getPokemonDao().getPokemon(pokemonName)
    }

    suspend fun getPokemon(pokemonName: String): Resource<Pokemon> {
        return repository.getPokemonInfo(pokemonName)
    }
}