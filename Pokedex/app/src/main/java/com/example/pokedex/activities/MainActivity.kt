package com.example.pokedex.activities

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.pokedex.screens.PokemonListScreen
import com.example.pokedex.screens.PokemonScreen
import com.example.pokedex.viewmodels.PokemonListViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Surface(color = MaterialTheme.colors.background,
                modifier = Modifier.fillMaxSize(),
                    ) {
                val state = rememberLazyGridState()
                val navController = rememberNavController()
                NavHost(
                    navController = navController,
                    startDestination = "pokemon_list_screen"
                ) {
                    composable("pokemon_list_screen") {
                        PokemonListScreen(
                            navController = navController,
                            state = state,
                            viewModel = hiltViewModel<PokemonListViewModel>()
                        )
                    }
                    composable(
                        "pokemon_screen/{pokemonName}",
                        arguments = listOf(
                            navArgument("pokemonName") {
                                type = NavType.StringType
                            }
                        )
                    ) {
                        val pokemonName = remember {
                            it.arguments?.getString("pokemonName")
                        }
                        if (pokemonName != null) {
                            PokemonScreen(
                                pokemonName = pokemonName.lowercase(Locale.ROOT) ,
                                navController = navController
                            )
                        }
                    }
                }
            }
        }
    }
}