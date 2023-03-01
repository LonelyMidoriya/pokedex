package com.example.pokedex.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.example.pokedex.data.models.PokemonListEntry
import com.example.pokedex.data.models.DBPokemon
import com.example.pokedex.util.Converters
import com.example.pokedex.database.PokemonDatabase
import com.example.pokedex.database.RemoteKeys
import com.example.pokedex.util.Constants
import com.example.pokedex.util.Constants.IMAGE_URL
import retrofit2.HttpException
import java.io.IOException
import java.util.*
import java.util.concurrent.TimeUnit

@OptIn(ExperimentalPagingApi::class)
class PokemonRemoteMediator (
    private val pokemonRepository: PokemonRepository,
    private val pokemonDatabase: PokemonDatabase,
    private var currentPage: Int = 0

): RemoteMediator<Int, PokemonListEntry>() {

    override suspend fun initialize(): InitializeAction {
        val cacheTimeout = TimeUnit.MILLISECONDS.convert(1, TimeUnit.HOURS)

        return if (System.currentTimeMillis() - (pokemonDatabase.getRemoteKeysDao().getCreationTime() ?: 0) < cacheTimeout) {
            InitializeAction.SKIP_INITIAL_REFRESH
        } else {
            InitializeAction.LAUNCH_INITIAL_REFRESH
        }
    }

    private suspend fun getRemoteKeyForLastItem(state: PagingState<Int, PokemonListEntry>): RemoteKeys? {
        return state.pages.lastOrNull {
            it.data.isNotEmpty()
        }?.data?.lastOrNull()?.let { entry ->
            pokemonDatabase.getRemoteKeysDao().getRemoteKeyByPokemonID(entry.number)
        }
    }

    private suspend fun getRemoteKeyForFirstItem(state: PagingState<Int, PokemonListEntry>): RemoteKeys? {
        return state.pages.firstOrNull {
            it.data.isNotEmpty()
        }?.data?.firstOrNull()?.let { entry ->
            pokemonDatabase.getRemoteKeysDao().getRemoteKeyByPokemonID(entry.number)
        }
    }


    private suspend fun getRemoteKeyClosestToCurrentPosition(state: PagingState<Int, PokemonListEntry>): RemoteKeys? {
        return state.anchorPosition?.let { position ->
            state.closestItemToPosition(position)?.number?.let { number ->
                pokemonDatabase.getRemoteKeysDao().getRemoteKeyByPokemonID(number)
            }
        }
    }


    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, PokemonListEntry>
    ): MediatorResult {
        when (loadType) {
            LoadType.REFRESH -> {
                val remoteKeys = getRemoteKeyClosestToCurrentPosition(state)
                remoteKeys?.nextKey?.minus(1) ?: 1
            }
            LoadType.PREPEND -> {
                val remoteKeys = getRemoteKeyForFirstItem(state)
                val prevKey = remoteKeys?.prevKey
                prevKey ?: return MediatorResult.Success(endOfPaginationReached = remoteKeys != null)
            }
            LoadType.APPEND -> {

                val remoteKeys = getRemoteKeyForLastItem(state)
                val nextKey = remoteKeys?.nextKey
                nextKey ?: return MediatorResult.Success(endOfPaginationReached = remoteKeys != null)
                currentPage++
            }
        }

        try {
            val apiResponsePokemonList = pokemonRepository.getPokemonList(
                Constants.PAGE_SIZE,
                (currentPage) * Constants.PAGE_SIZE
            )
            val pokemons: MutableList<DBPokemon> = mutableListOf()
            val endOfPaginationReached: Boolean
            if (apiResponsePokemonList.data==null){
                endOfPaginationReached = true
            }
            else{
                val converters = Converters()
                val pokemonEntries = apiResponsePokemonList.data.results.mapIndexed { _, entry ->
                    val number = if (entry.url.endsWith("/")) {
                        entry.url.dropLast(1).takeLastWhile { it.isDigit() }
                    } else {
                        entry.url.takeLastWhile { it.isDigit() }
                    }
                    val url = "$IMAGE_URL$number.png"

                    val apiResponsePokemon = pokemonRepository.getPokemonInfo(entry.name)

                    apiResponsePokemon.data?.let { converters.pokemonToDBPokemon(it) }
                        ?.let { pokemons.add(it) }

                    PokemonListEntry(entry.name.replaceFirstChar {
                        if (it.isLowerCase()) it.titlecase(
                            Locale.ROOT
                        ) else it.toString()
                    }, url, number.toInt())
                }

                endOfPaginationReached = pokemonEntries.isEmpty()

                pokemonDatabase.withTransaction {
                    if (loadType == LoadType.REFRESH) {
                        pokemonDatabase.getRemoteKeysDao().clearRemoteKeys()
                        pokemonDatabase.getEntriesDao().clearAllEntries()
                        pokemonDatabase.getPokemonDao().clearAllPokemons()
                    }
                    val prevKey = if (currentPage > 0) currentPage - 1 else null
                    val nextKey = if (endOfPaginationReached) null else currentPage + 1
                    val remoteKeys = pokemonEntries.map {
                        RemoteKeys(
                            pokemonID = it.number,
                            prevKey = prevKey,
                            currentPage = currentPage,
                            nextKey = nextKey
                        )
                    }
                    pokemonDatabase.getRemoteKeysDao().insertAll(remoteKeys)
                    pokemonDatabase.getEntriesDao().insertAll(
                        pokemonEntries.onEachIndexed {
                                index, pokemon -> pokemon.number = index + currentPage * Constants.PAGE_SIZE + 1}
                    )
                    pokemonDatabase.getPokemonDao().insertAll(pokemons)
                }
            }
            return MediatorResult.Success(endOfPaginationReached = endOfPaginationReached)
        } catch (error: IOException) {
            return MediatorResult.Error(error)
        } catch (error: HttpException) {
            return MediatorResult.Error(error)
        }
    }
}