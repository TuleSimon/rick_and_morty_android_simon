package com.simon.rickandmorty.features.home

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridItemSpan
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.simon.domain.entities.MovieCharacter
import com.simon.rickandmorty.R
import com.simon.rickandmorty.core.dummyMovieCharacters
import com.simon.rickandmorty.core.shimmer
import com.simon.rickandmorty.core.theme.RickAndMortyTheme
import com.simon.rickandmorty.features.home.components.CharacterCard
import kotlinx.collections.immutable.toPersistentList
import kotlinx.serialization.Serializable

@Serializable
object HomeScreenRoute

@Composable
fun AnimatedVisibilityScope.HomeScreen(
    homeScreenViewModel: HomeScreenViewModel = hiltViewModel(),
    onNavigateToDetails: (MovieCharacter) -> Unit = {}
) {

    val state = homeScreenViewModel.state.collectAsStateWithLifecycle()

    HomeScreenContent(state.value, onFetchMoreCharacters = {
        homeScreenViewModel.fetchMoreCharacters()
    }, onNavigateToDetails = {
        onNavigateToDetails(it)
    })

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AnimatedVisibilityScope.HomeScreenContent(
    state: HomeScreenState,
    onFetchMoreCharacters: () -> Unit = {},
    onNavigateToDetails: (MovieCharacter) -> Unit = {}
) {

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(stringResource(R.string.characters))
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = Color.Transparent
                )
            )
        },
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Box(
            Modifier
                .fillMaxSize()
                .navigationBarsPadding()
                .padding(it)
                .padding(horizontal = 16.dp),
            contentAlignment = Alignment.Center
        ) {

            if (state.characters.isEmpty() && !state.isFetchingInitialData) {
                Text(
                    stringResource(R.string.no_characters_found),
                    style = MaterialTheme.typography.titleLarge
                )
            }
            if (state.hasError && !state.isFetchingInitialData) {
                Text(
                    stringResource(R.string.failed_to_load_characters),
                    style = MaterialTheme.typography.titleLarge
                )
            }

            LazyVerticalStaggeredGrid(
                StaggeredGridCells.Fixed(2),
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalItemSpacing = 10.dp
            ) {
                if (state.isFetchingInitialData) {
                    items(12) {
                        Box(
                            Modifier
                                .fillMaxWidth()
                                .aspectRatio(1.2f)
                                .shimmer(true)
                        )
                    }
                }

                items(state.characters) { character ->
                    CharacterCard(
                        character,
                        modifier = Modifier
                            .fillMaxWidth()
                            .animateItem()
                    ) {
                        onNavigateToDetails(character)
                    }
                }
                if(state.isFetchingMoreData) {
                    item(span = StaggeredGridItemSpan.FullLine) {
                        Box(
                            contentAlignment = Alignment.Center,
                        ) {
                            CircularProgressIndicator(Modifier.size(30.dp))
                        }
                    }
                }

                item {
                    LaunchedEffect(true) {
                        if (state.hasMore && !state.isFetchingMoreData) {
                            onFetchMoreCharacters()
                        }
                    }
                }
            }
        }

    }

}

@Preview(showBackground = true)
@Composable
private fun HomeScreenPreview() {
    RickAndMortyTheme() {
        AnimatedVisibility(true) {
            HomeScreenContent(
                HomeScreenState(
                    characters = dummyMovieCharacters.toPersistentList(),
                    isFetchingInitialData = true,
                    hasError = true
                )
            )
        }
    }
}