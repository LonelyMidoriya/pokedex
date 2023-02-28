package com.example.pokedex.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.*
import com.example.pokedex.data.models.PokemonListEntry
import com.example.pokedex.database.PokemonDatabase
import com.example.pokedex.repository.PokemonRemoteMediator
import com.example.pokedex.repository.PokemonRepository
import com.example.pokedex.util.Constants.PAGE_SIZE
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject


@HiltViewModel
class PokemonListViewModel @Inject constructor(
    private val pokemonRepository: PokemonRepository,
    private val pokemonDatabase: PokemonDatabase,
): ViewModel() {
    @OptIn(ExperimentalPagingApi::class)
    fun getPokemonList(): Flow<PagingData<PokemonListEntry>> =
        Pager(
            config = PagingConfig(
                pageSize = PAGE_SIZE,
                prefetchDistance = 10,
            ),
            pagingSourceFactory = {
                pokemonDatabase.getEntriesDao().getEntries()
            },
            remoteMediator = PokemonRemoteMediator(
                pokemonRepository,
                pokemonDatabase,
            )
        ).flow.cachedIn(viewModelScope)
}