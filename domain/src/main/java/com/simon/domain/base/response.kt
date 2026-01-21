package com.simon.domain.base

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class BaseResponse<T>(
    @SerialName("results")
    val results: List<T>,

    @SerialName("info")
    val info: PageInfo? = null
)

@Serializable
data class PageInfo(
    val count: Int,
    val pages: Int,
    val next: String? = null,
    val prev: String? = null
)


sealed interface BaseResult<out T> {

    data class Success<T>(val data: T) : BaseResult<T>

    data class Error<T>(
        val message: String,
        val errorBody: Any? = null
    ) : BaseResult<T>

    data class Loading<T>(val progress:Float=0f) : BaseResult<T>{
        override fun toString(): String {
            return "ResultLoading"
        }
    }
    object NetworkError : BaseResult<Nothing>{
        override fun toString(): String {
            return "ResultNetworkError"
        }
    }

    object NoInternet: BaseResult<Nothing>{
        override fun toString(): String {
            return "ResultNoInternetError"
        }
    }

}

fun BaseResult<*>.isLoading() = this is BaseResult.Loading
fun BaseResult<*>.isSuccess() = this is BaseResult.Success
fun BaseResult<*>.isNetworkError() = this is BaseResult.NetworkError

fun <T> BaseResult<T>.getOrNull(): T? =
    (this as? BaseResult.Success)?.data

