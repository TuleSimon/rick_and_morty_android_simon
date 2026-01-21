package com.simon.data.network.datasources.character


import com.simon.data.network.apiRoutes.BaseUrls
import com.simon.data.network.base.CallParams
import com.simon.data.network.base.NetworkCall
import com.simon.data.network.dtos.character.CharacterItemResponseDto
import com.simon.domain.base.BaseResponse
import com.simon.domain.base.BaseResult
import io.ktor.util.reflect.typeInfo
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class CharacterDataSourceImpl @Inject constructor(
    private val networkCall: NetworkCall,
) : CharacterDataSource {
    override suspend fun fetchCharacters(page: Int): Flow<BaseResult<BaseResponse<CharacterItemResponseDto>>> {
        return networkCall.getFlow(
            BaseUrls.CHARACTER_URL,
            typeInfo<BaseResponse<CharacterItemResponseDto>>(),
            queryParams = mapOf("page" to page.toString()),
            callParams = CallParams(checkNetwork = true, useCache = true),
        )
    }

    override suspend fun fetchCharacterDetails(characterId: Int): Flow<BaseResult<CharacterItemResponseDto>> {
        return networkCall.getFlow(
            BaseUrls.CHARACTER_URL + "/$characterId",
            typeInfo<CharacterItemResponseDto>(),
        )
    }
}