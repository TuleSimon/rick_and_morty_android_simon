package com.simon.data.network.datasources.character

import com.simon.data.network.dtos.character.CharacterItemResponseDto
import com.simon.domain.base.BaseResponse
import com.simon.domain.base.BaseResult
import kotlinx.coroutines.flow.Flow

interface CharacterDataSource {
    suspend fun fetchCharacters(page: Int): Flow<BaseResult<BaseResponse<CharacterItemResponseDto>>>
    suspend fun fetchCharacterDetails(characterId: Int): Flow<BaseResult<CharacterItemResponseDto>>
}