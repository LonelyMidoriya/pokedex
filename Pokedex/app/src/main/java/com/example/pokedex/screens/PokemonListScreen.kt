package com.example.pokedex.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.rounded.Warning
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.layout.ContentScale
import androidx.navigation.NavController
import androidx.paging.compose.LazyPagingItems
import coil.compose.AsyncImagePainter
import coil.compose.rememberAsyncImagePainter
import com.example.pokedex.data.models.PokemonListEntry
import com.example.pokedex.viewmodels.PokemonListViewModel
import java.util.*


@Composable
fun PokemonListScreen(
    navController: NavController,
    state: LazyGridState,
    viewModel: PokemonListViewModel,
    entries: LazyPagingItems<PokemonListEntry>? = viewModel.getPokemonList().collectAsLazyPagingItems()
) {
        LazyVerticalGrid(
            columns =  GridCells.Fixed(2),
            horizontalArrangement = Arrangement.spacedBy(20.dp),
            state = state,
            contentPadding = PaddingValues(
                start = 12.dp,
                top = 16.dp,
                end = 12.dp,
                bottom = 16.dp
            ),
            content = {
                items(
                entries!!.itemCount
            ) { index ->
                PokemonCard(index = index, entries = entries, navController = navController)
            }
            item {
                val loadState = entries.loadState.mediator
                if (loadState?.refresh == LoadState.Loading) {
                Column(
                    modifier = Modifier.fillMaxWidth().fillMaxHeight()
                        ,
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                ) {
                    Text(
                        modifier = Modifier
                            .padding(8.dp),
                        text = "Refresh Loading"
                    )
                    CircularProgressIndicator(color = MaterialTheme.colors.primary)
                }
            }
                if (loadState?.append == LoadState.Loading) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        contentAlignment = Alignment.Center,
                    ) {
                        CircularProgressIndicator(color = MaterialTheme.colors.primary)
                    }
                }

                if (loadState?.refresh is LoadState.Error || loadState?.append is LoadState.Error) {
                    val isPaginatingError = (loadState.append is LoadState.Error) || entries.itemCount > 1
                    val error = if (loadState.append is LoadState.Error)
                        (loadState.append as LoadState.Error).error
                    else
                        (loadState.refresh as LoadState.Error).error

                    val modifier = if (isPaginatingError) {
                        Modifier.padding(8.dp)
                    } else {
                        Modifier.fillMaxSize()
                    }
                    Column(
                        modifier = modifier,
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {
                        if (!isPaginatingError) {
                            Icon(
                                modifier = Modifier
                                    .size(64.dp),
                                imageVector = androidx.compose.material.icons.Icons.Rounded.Warning, contentDescription = null
                            )
                        }
                        Text(
                            modifier = Modifier
                                .padding(8.dp),
                            text = error.message ?: error.toString(),
                            textAlign = TextAlign.Center,
                        )
                        Button(
                            onClick = {
                                entries.refresh()
                            },
                            content = {
                                Text(text = "Refresh")
                            },
                            colors = ButtonDefaults.buttonColors(
                                backgroundColor = MaterialTheme.colors.primary,
                                contentColor = Color.White,
                            )
                        )
                    }
                }
            }
            }
        )
}

@Composable
fun PokemonEntry(
    entry: PokemonListEntry,
    modifier: Modifier = Modifier,
) {
    Box(contentAlignment = Alignment.Center,
        modifier = modifier
            .shadow(5.dp, RoundedCornerShape(10.dp))
            .clip(RoundedCornerShape(10.dp))
            .aspectRatio(1f)
            .background(
                Color.LightGray
            )
    )
    {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
            )
        {
            if (entry.imageUrl != null) {
                var isImageLoading by remember { mutableStateOf(false)
                }
            val painter = rememberAsyncImagePainter(entry.imageUrl)
            isImageLoading = when(painter.state) {
                is AsyncImagePainter.State.Loading -> true
                else -> false
            }
            Box (
                contentAlignment = Alignment.Center,
            ) {
                Image(
                    modifier = Modifier
                        .padding(horizontal = 6.dp, vertical = 3.dp)
                        .height(115.dp)
                        .width(115.dp)
                        .clip(RoundedCornerShape(8.dp)),
                    painter = painter,
                    contentDescription = "Image",
                    contentScale = ContentScale.FillBounds,
                )
                if (isImageLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier
                            .padding(horizontal = 6.dp, vertical = 3.dp),
                        color = MaterialTheme.colors.primary,
                    )
                }
            }
        }
        Text(
            modifier = Modifier
                .padding(horizontal = 8.dp),
            text = entry.pokemonName
        )
        }
    }
}

@Composable
fun PokemonCard(
    index: Int,
    entries: LazyPagingItems<PokemonListEntry>,
    navController: NavController,
) {
    Column {
        Row {
            entries[index]?.let {
                PokemonEntry(
                    entry = it,
                    modifier = Modifier.weight(1f)
                        .clickable() {
                            navController.navigate(
                                "pokemon_screen/${it.pokemonName.lowercase(Locale.ROOT)}"
                            )
                        }
                )
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
    }
}