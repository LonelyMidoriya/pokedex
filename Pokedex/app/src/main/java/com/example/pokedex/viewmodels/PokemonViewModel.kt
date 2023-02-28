package com.example.pokedex.viewmodels

import androidx.lifecycle.ViewModel
import com.example.pokedex.data.remote.responses.DBPokemon
import com.example.pokedex.database.PokemonDatabase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class PokemonViewModel @Inject constructor(
    private val pokemonDatabase: PokemonDatabase,
) : ViewModel() {
    fun getPokemonInfo(pokemonName: String): DBPokemon {
           return pokemonDatabase.getPokemonDao().getPokemon(pokemonName)
    }
}