package com.simon.data.network.connectivity


interface Connectivity {
    suspend fun isNetworkAvailable():Boolean
}