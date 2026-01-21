package com.simon.rickandmorty.core.navgraph

import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.simon.domain.entities.MovieCharacter
import com.simon.rickandmorty.core.fromJson
import com.simon.rickandmorty.core.toJson
import com.simon.rickandmorty.features.details.CharacterDetailsScreen
import com.simon.rickandmorty.features.details.DetailsScreenRoute
import com.simon.rickandmorty.features.home.HomeScreen
import com.simon.rickandmorty.features.home.HomeScreenRoute

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun DefaultNavGraph(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    SharedTransitionLayout(
        modifier = Modifier.background(MaterialTheme.colorScheme.background),
    ) {
        CompositionLocalProvider(
            LocalSharedTransitionScope provides this,
        ) {

            NavHost(
                navController = navController,
                startDestination = HomeScreenRoute,
                modifier = modifier.fillMaxSize(),
            ) {
                composable<HomeScreenRoute> {
                    HomeScreen {
                        navController.navigate(DetailsScreenRoute(it.id, it.toJson()))
                    }
                }
                composable<DetailsScreenRoute> {
                    val characterId = it.toRoute<DetailsScreenRoute>().characterId
                    val characterJson =
                        it.toRoute<DetailsScreenRoute>().characterJson?.fromJson<MovieCharacter>()
                    CharacterDetailsScreen(characterId, characterJson) {
                        navController.navigateUp()
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalSharedTransitionApi::class)
val LocalSharedTransitionScope = compositionLocalOf<SharedTransitionScope?> { null }