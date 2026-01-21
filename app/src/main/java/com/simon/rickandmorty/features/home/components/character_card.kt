package com.simon.rickandmorty.features.home.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.simon.domain.entities.MovieCharacter
import com.simon.rickandmorty.core.dummyMovieCharacters
import com.simon.rickandmorty.core.navgraph.LocalSharedTransitionScope
import com.simon.rickandmorty.core.theme.RickAndMortyTheme

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun AnimatedVisibilityScope.CharacterCard(
    character: MovieCharacter,
    modifier: Modifier = Modifier,
    onNavigate: () -> Unit
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable {
                onNavigate()
            },
        shape = RoundedCornerShape(20.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
    ) {
        Column {
            val sharedElementSCope = LocalSharedTransitionScope.current!!
            with(sharedElementSCope) {
                AsyncImage(
                    model = character.imageUrl,
                    contentDescription = character.name,
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(1.2f)
                        .clip(
                            RoundedCornerShape(
                                topStart = 20.dp,
                                topEnd = 20.dp
                            )
                        )
                        .background(Color.LightGray)
                        .sharedElement(
                            rememberSharedContentState(key = character.imageUrl),
                            animatedVisibilityScope = this@CharacterCard,
                        ),
                    contentScale = ContentScale.Crop
                )
            }


            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {

                Text(
                    text = character.name,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(8.dp))


                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(10.dp)
                            .clip(CircleShape)
                            .background(statusColor(character.status.toString()))
                    )

                    Spacer(modifier = Modifier.width(8.dp))

                    Text(
                        text = character.status.toString(),
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.Gray
                    )

                    Text(
                        text = " • ${character.species}",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.Gray
                    )
                }
            }
        }
    }
}

fun statusColor(status: String): Color =
    when (status.lowercase()) {
        "alive" -> Color(0xFF4CAF50)
        "dead" -> Color(0xFFF44336)
        else -> Color(0xFF9E9E9E)
    }


@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
@Preview(showBackground = true)
private fun CharacterPreview() {
    RickAndMortyTheme() {
        SharedTransitionLayout(
            modifier = Modifier.background(MaterialTheme.colorScheme.background),
        ) {
            CompositionLocalProvider(
                LocalSharedTransitionScope provides this,
            ) {
                AnimatedVisibility(true) {
                    CharacterCard(dummyMovieCharacters.first()) {}
                }
            }
        }
    }
}