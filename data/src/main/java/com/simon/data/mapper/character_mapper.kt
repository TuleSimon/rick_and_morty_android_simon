package com.simon.data.mapper

import com.simon.data.network.dtos.character.CharacterItemResponseDto
import com.simon.domain.entities.*

fun CharacterItemResponseDto.toDomain(): MovieCharacter =
    MovieCharacter(
        id = id,
        name = name,
        imageUrl = image,
        status = status.toCharacterStatus(),
        species = species,
        gender = gender,
        origin = origin.toDomain(),
        location = location.toDomain(),
        episodeCount = episode.size
    )

fun CharacterItemResponseDto.Origin.toDomain(): Place =
    Place(
        name = name,
        url = url
    )

fun CharacterItemResponseDto.Location.toDomain(): Place =
    Place(
        name = name,
        url = url
    )

private fun String.toCharacterStatus(): CharacterStatus =
    when (lowercase()) {
        "alive" -> CharacterStatus.ALIVE
        "dead" -> CharacterStatus.DEAD
        else -> CharacterStatus.UNKNOWN
    }
