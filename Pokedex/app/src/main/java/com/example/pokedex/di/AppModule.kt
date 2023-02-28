package com.example.pokedex.di
import android.content.Context
import androidx.room.Room
import com.example.pokedex.data.remote.PokeApi
import com.example.pokedex.database.EntriesDao
import com.example.pokedex.database.PokemonDao
import com.example.pokedex.database.PokemonDatabase
import com.example.pokedex.database.RemoteKeysDao
import com.example.pokedex.repository.PokemonRepository
import com.example.pokedex.util.Constants.BASE_URL
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun providePokemonRepository(
        api: PokeApi
    ) = PokemonRepository(api)

    @Singleton @Provides
    fun providePokeApi(): PokeApi {
        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(BASE_URL)
            .build()
            .create(PokeApi::class.java)
    }

    @Singleton
    @Provides
    fun providePokemonDatabase(@ApplicationContext context: Context): PokemonDatabase =
        Room
            .databaseBuilder(context, PokemonDatabase::class.java, "pokemon_database12")
            .allowMainThreadQueries()
            .build()

    @Singleton
    @Provides
    fun provideMoviesDao(pokemonDatabase: PokemonDatabase): EntriesDao = pokemonDatabase.getEntriesDao()

    @Singleton
    @Provides
    fun providePokemonDao(pokemonDatabase: PokemonDatabase): PokemonDao = pokemonDatabase.getPokemonDao()

    @Singleton
    @Provides
    fun provideRemoteKeysDao(pokemonDatabase: PokemonDatabase): RemoteKeysDao = pokemonDatabase.getRemoteKeysDao()

}