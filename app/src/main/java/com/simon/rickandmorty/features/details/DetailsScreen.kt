package com.simon.rickandmorty.features.details


import android.graphics.drawable.BitmapDrawable
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.palette.graphics.Palette
import coil.compose.AsyncImagePainter
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.simon.domain.entities.MovieCharacter
import com.simon.rickandmorty.core.dummyMovieCharacters
import com.simon.rickandmorty.core.navgraph.LocalSharedTransitionScope
import com.simon.rickandmorty.core.theme.RickAndMortyTheme
import com.simon.rickandmorty.features.home.components.statusColor
import kotlinx.serialization.Serializable

@Serializable
data class DetailsScreenRoute(
    val characterId: Int,
    val characterJson: String? = null
)

@Composable
fun AnimatedVisibilityScope.CharacterDetailsScreen(
    character: Int,
    characterJson: MovieCharacter? = null,
    detailsViewModel: DetailsScreenViewModel = hiltViewModel(),
    onNavigate: () -> Unit
) {
    val state = detailsViewModel.state.collectAsStateWithLifecycle()
    LaunchedEffect(true) {
        detailsViewModel.initCharacterUseCase(character, characterJson)
    }
    CharacterDetailsScreenContent(state.value, onNavigate)
}

@OptIn(ExperimentalSharedTransitionApi::class, ExperimentalMaterial3Api::class)
@Composable
private fun AnimatedVisibilityScope.CharacterDetailsScreenContent(
    state: DetailsScreenState,
    onNavigate: () -> Unit
) {
    Surface {
        AnimatedContent(state.characters == null || state.isFetchingDetails) {

            if (it) {
                Box(Modifier.fillMaxWidth()) {
                    CircularProgressIndicator(Modifier.size(40.dp))
                }
            } else {
                val character = state.characters!!
                val scope = LocalSharedTransitionScope.current
                with(scope!!) {
                    val surfaceColor = MaterialTheme.colorScheme.surface
                    var dominantColor by remember {
                        mutableStateOf(surfaceColor)
                    }

                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .verticalScroll(rememberScrollState())
                    ) {

                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(280.dp)
                                .background(dominantColor),
                            contentAlignment = Alignment.BottomCenter
                        ) {

                            CharacterImage(
                                imageUrl = character.imageUrl,
                                modifier = Modifier
                                    .fillMaxHeight()
                                    .sharedElement(
                                        rememberSharedContentState(key = character.imageUrl),
                                        animatedVisibilityScope = this@CharacterDetailsScreenContent,
                                    ),
                                onDominantColorExtracted = { dominantColor = it }
                            )
                            TopAppBar(
                                title = {
                                    Text("")
                                },
                                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                                    containerColor = Color.Transparent
                                ),
                                modifier = Modifier
                                    .align(Alignment.TopStart)
                                    .padding(
                                        horizontal = 16.dp
                                    ),
                                navigationIcon = {
                                    Icon(
                                        Icons.AutoMirrored.Outlined.ArrowBack,
                                        contentDescription = null,
                                        modifier = Modifier
                                            .size(35.dp)
                                            .background(Color.Black.copy(0.3f), CircleShape)
                                            .padding(5.dp)
                                            .clickable {
                                                onNavigate()
                                            },
                                        tint = Color.White
                                    )
                                }
                            )
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        Column(
                            modifier = Modifier.padding(16.dp)
                        ) {
                            Text(
                                text = character.name,
                                style = MaterialTheme.typography.headlineMedium,
                                fontWeight = FontWeight.Bold
                            )

                            Spacer(modifier = Modifier.height(8.dp))

                            Text(
                                text = character.status.name,
                                color = statusColor(character.status.toString())
                            )

                            Spacer(modifier = Modifier.height(16.dp))

                            CharacterDetailItem("Species", character.species)
                            CharacterDetailItem("Gender", character.gender)
                            CharacterDetailItem("Origin", character.origin.name)
                            CharacterDetailItem("Location", character.location.name)
                            CharacterDetailItem("Episodes", character.episodeCount.toString())
                        }
                    }
                }
            }
        }
    }

}

@Composable
private fun CharacterImage(
    imageUrl: String,
    modifier: Modifier = Modifier,
    onDominantColorExtracted: (Color) -> Unit
) {
    val context = LocalContext.current

    val painter = rememberAsyncImagePainter(
        model = ImageRequest.Builder(context)
            .data(imageUrl)
            .allowHardware(false)
            .build()
    )

    val painterState = painter.state

    if (painterState is AsyncImagePainter.State.Success) {
        val bitmap = (painterState.result.drawable as BitmapDrawable).bitmap

        LaunchedEffect(bitmap) {
            Palette.from(bitmap).generate { palette ->
                palette?.dominantSwatch?.rgb?.let {
                    onDominantColorExtracted(Color(it))
                }
            }
        }
    }

    Image(
        painter = painter,
        modifier = modifier,
        contentDescription = "Character Image",
        contentScale = ContentScale.FillHeight
    )
}

@Composable
fun CharacterDetailItem(
    label: String,
    value: String,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            color = Color.Gray
        )

        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Medium,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@OptIn(ExperimentalSharedTransitionApi::class)
@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun MovieCharacterDetailsPreview() {
    RickAndMortyTheme() {
        SharedTransitionLayout(
            modifier = Modifier.background(MaterialTheme.colorScheme.background),
        ) {
            CompositionLocalProvider(
                LocalSharedTransitionScope provides this,
            ) {
                AnimatedVisibility(true) {
                    CharacterDetailsScreenContent(DetailsScreenState(dummyMovieCharacters.first())) {}
                }
            }
        }
    }
}

