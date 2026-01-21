package com.simon.domain.usecase

import com.simon.domain.base.BaseResult
import kotlinx.coroutines.flow.Flow


interface BaseUseCase<out Type, in Params> {

    suspend fun execute(params: Params): Flow<BaseResult<Type>>
}
