package com.simon.domain.features.character

import com.simon.domain.base.BaseResult
import com.simon.domain.entities.MovieCharacter
import com.simon.domain.usecase.BaseUseCase
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetCharacterDetailsUseCase @Inject constructor(
    private val characterRepository: CharacterRepository
) : BaseUseCase<MovieCharacter, Int> {
    override suspend fun execute(params: Int): Flow<BaseResult<MovieCharacter>> {
        return characterRepository.fetchCharacterDetails(params)
    }
}