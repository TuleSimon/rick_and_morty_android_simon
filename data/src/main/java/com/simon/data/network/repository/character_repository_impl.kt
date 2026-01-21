package com.simon.data.network.repository

import com.simon.data.mapper.toDomain
import com.simon.data.network.datasources.character.CharacterDataSource
import com.simon.data.network.toEntity
import com.simon.domain.base.BaseResponse
import com.simon.domain.base.BaseResult
import com.simon.domain.entities.MovieCharacter
import com.simon.domain.features.character.CharacterRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class CharacterRepositoryImpl @Inject constructor(
    private val characterDataSource: CharacterDataSource
) : CharacterRepository {
    override suspend fun fetchCharacters(page: Int): Flow<BaseResult<BaseResponse<MovieCharacter>>> {
        val result = characterDataSource.fetchCharacters(page).toEntity { response ->
            return@toEntity BaseResponse(
                results = response.results.map { it.toDomain() },
                info = response.info
            )
        }
        return result
    }

    override suspend fun fetchCharacterDetails(characterId: Int): Flow<BaseResult<MovieCharacter>> {
        val result = characterDataSource.fetchCharacterDetails(characterId).toEntity {
            return@toEntity it.toDomain()
        }
        return result
    }

}