package com.simon.rickandmorty.core

import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.dp
import com.simon.data.di.json
import com.simon.domain.entities.CharacterStatus
import com.simon.domain.entities.MovieCharacter
import com.simon.domain.entities.Place
import kotlinx.serialization.encodeToString

@Composable
fun Modifier.shimmer(
    visible: Boolean,
    shape: Shape = RoundedCornerShape(12.dp),
    color: Color = MaterialTheme.colorScheme.surfaceContainer,
    highlightColor:Color = Color.Gray.copy(0.4f),
): Modifier {
    return widthIn(min = 10.dp).placeholder(
        visible, color, shape, PlaceholderHighlight.shimmer(
            highlightColor = highlightColor
        ) as PlaceholderHighlight?
    )
}


val dummyMovieCharacters = listOf(
    MovieCharacter(
        id = 1,
        name = "Rick Sanchez",
        imageUrl = "https://rickandmortyapi.com/api/character/avatar/1.jpeg",
        status = CharacterStatus.ALIVE,
        species = "Human",
        gender = "Male",
        origin = Place(
            name = "Earth (C-137)",
            url = "https://rickandmortyapi.com/api/location/1"
        ),
        location = Place(
            name = "Citadel of Ricks",
            url = "https://rickandmortyapi.com/api/location/3"
        ),
        episodeCount = 51
    ),

    MovieCharacter(
        id = 2,
        name = "Morty Smith",
        imageUrl = "https://rickandmortyapi.com/api/character/avatar/2.jpeg",
        status = CharacterStatus.ALIVE,
        species = "Human",
        gender = "Male",
        origin = Place(
            name = "unknown",
            url = ""
        ),
        location = Place(
            name = "Citadel of Ricks",
            url = "https://rickandmortyapi.com/api/location/3"
        ),
        episodeCount = 51
    ),

    MovieCharacter(
        id = 3,
        name = "Summer Smith",
        imageUrl = "https://rickandmortyapi.com/api/character/avatar/3.jpeg",
        status = CharacterStatus.ALIVE,
        species = "Human",
        gender = "Female",
        origin = Place(
            name = "Earth (Replacement Dimension)",
            url = "https://rickandmortyapi.com/api/location/20"
        ),
        location = Place(
            name = "Earth (Replacement Dimension)",
            url = "https://rickandmortyapi.com/api/location/20"
        ),
        episodeCount = 43
    )
)

inline fun <reified T> T?.toJson():String?{
    return try {
        json.encodeToString(this)
    }
    catch (e:Exception){
        e.printStackTrace()
        null
    }
}

inline fun <reified T> String?.fromJson(): T? {
    return try {
        this?.let { json.decodeFromString<T>(it) }
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}