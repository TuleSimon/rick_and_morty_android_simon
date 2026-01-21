package com.simon.domain.features.character

import com.simon.domain.base.BaseResponse
import com.simon.domain.base.BaseResult
import com.simon.domain.entities.MovieCharacter
import kotlinx.coroutines.flow.Flow

interface CharacterRepository {
    suspend fun fetchCharacters(page: Int): Flow<BaseResult<BaseResponse<MovieCharacter>>>
    suspend fun fetchCharacterDetails(characterId: Int): Flow<BaseResult<MovieCharacter>>
}