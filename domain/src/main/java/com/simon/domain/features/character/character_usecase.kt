package com.simon.domain.features.character

import com.simon.domain.base.BaseResponse
import com.simon.domain.base.BaseResult
import com.simon.domain.entities.MovieCharacter
import com.simon.domain.usecase.BaseUseCase
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetCharacterUseCase @Inject constructor(
    private val characterRepository: CharacterRepository
) : BaseUseCase<BaseResponse<MovieCharacter>, Int> {
    override suspend fun execute(params: Int): Flow<BaseResult<BaseResponse<MovieCharacter>>> {
        return characterRepository.fetchCharacters(params)
    }
}