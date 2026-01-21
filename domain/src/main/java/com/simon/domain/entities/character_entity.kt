package com.simon.domain.entities

import kotlinx.serialization.Serializable

@Serializable
data class MovieCharacter(
    val id: Int,
    val name: String,
    val imageUrl: String,
    val status: CharacterStatus,
    val species: String,
    val gender: String,
    val origin: Place,
    val location: Place,
    val episodeCount: Int
)

@Serializable
data class Place(
    val name: String,
    val url: String
)

@Serializable
enum class CharacterStatus {
    ALIVE,
    DEAD,
    UNKNOWN
}
