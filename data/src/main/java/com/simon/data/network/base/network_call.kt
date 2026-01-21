package com.simon.data.network.base


import android.net.Uri
import com.simon.domain.base.BaseResult
import io.ktor.util.reflect.TypeInfo
import kotlinx.coroutines.flow.Flow
import java.io.File

interface NetworkCall {
    suspend fun <T> get(
        route: String,
        type: TypeInfo,
        queryParams: Map<String, String> = mapOf(),
        headerParams: Map<String, String> = mapOf(),
        callParams: CallParams = CallParams()
    ): BaseResult<T>

    suspend fun <T, R> delete(
        route: String,
        type: TypeInfo,
        body: R?,
        bodytype: TypeInfo?,
        queryParams: Map<String, String> = mapOf(),
        headerParams: Map<String, String> = mapOf(),
        callParams: CallParams = CallParams()
    ): BaseResult<T>

    suspend fun <T, R> post(
        route: String,
        body: R?,
        bodytype: TypeInfo?,
        type: TypeInfo,
        queryParams: Map<String, String> = mapOf(),
        headerParams: Map<String, String> = mapOf(),
        callParams: CallParams = CallParams()
    ): BaseResult<T>

    suspend fun <T, R> put(
        route: String,
        body: R?,
        bodytype: TypeInfo?,
        type: TypeInfo,
        queryParams: Map<String, String> = mapOf(),
        headerParams: Map<String, String> = mapOf(),
        callParams: CallParams = CallParams()
    ): BaseResult<T>

    suspend fun <T, R> patch(
        route: String,
        body: R?,
        bodytype: TypeInfo?,
        type: TypeInfo,
        queryParams: Map<String, String> = mapOf(),
        headerParams: Map<String, String> = mapOf(),
        callParams: CallParams = CallParams()
    ): BaseResult<T>

    suspend fun <T> getFlow(
        route: String,
        type: TypeInfo,
        queryParams: Map<String, String> = mapOf(),
        headerParams: Map<String, String> = mapOf(),
        callParams: CallParams = CallParams()
    ): Flow<BaseResult<T>>


    suspend fun <T, R> deleteFlow(
        route: String,
        type: TypeInfo,
        body: R?,
        bodytype: TypeInfo?,
        queryParams: Map<String, String> = mapOf(),
        headerParams: Map<String, String> = mapOf(),
        callParams: CallParams = CallParams()
    ): Flow<BaseResult<T>>

    suspend fun <T, R> postFlow(
        route: String,
        body: R?,
        bodytype: TypeInfo?,
        type: TypeInfo,
        queryParams: Map<String, String> = mapOf(),
        headerParams: Map<String, String> = mapOf(),
        callParams: CallParams = CallParams()
    ): Flow<BaseResult<T>>

    suspend fun <T, R> putFlow(
        route: String,
        body: R?,
        bodytype: TypeInfo?,
        type: TypeInfo,
        queryParams: Map<String, String> = mapOf(),
        headerParams: Map<String, String> = mapOf(),
        callParams: CallParams = CallParams()
    ): Flow<BaseResult<T>>

}

data class FormDataHolder(
    val file: File? = null,
    val uri: Uri? = null,
    val name: String,
    val value: String? = null,
    val fileName: String? = null,
    val contentType: String? = null
)

data class CallParams(
    val checkNetwork: Boolean = true,
    val useCache: Boolean = false,
)
