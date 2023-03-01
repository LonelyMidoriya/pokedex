package com.example.pokedex.util

import com.example.pokedex.data.models.DBPokemon
import com.example.pokedex.data.models.Pokemon
import com.example.pokedex.data.models.Type
import com.example.pokedex.data.models.TypeX

class Converters {
    fun pokemonToDBPokemon(pokemon: Pokemon): DBPokemon {
        return DBPokemon(
            pokemon.id,
            pokemon.height,
            pokemon.name,
            getPokemonTypesString(pokemon.types),
            pokemon.weight,
        )
    }

    fun dbPokemonToPokemon(dbPokemon: DBPokemon) : Pokemon {
        return Pokemon(
            dbPokemon.height,
            dbPokemon.id,
            dbPokemon.name,
            getPokemonTypes(dbPokemon.types),
            dbPokemon.weight,
        )
    }

    private fun getPokemonTypesString(types: List<Type>) : String {
        val typesString = StringBuilder("")
        for(i in types.indices) {
            typesString.append(types[i].type.name)
            if(i != types.size-1){
                typesString.append(",")
            }
        }
        return typesString.toString()
    }

    private fun getPokemonTypes(typesString: String) : List<Type> {
        val typesStringList = typesString.split(",")
        val typesList = mutableListOf<Type>()
        typesStringList.forEach {
            typesList.add(Type(TypeX(it)))
        }
        return typesList
    }
}