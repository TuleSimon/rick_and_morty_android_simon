package com.simon.data.di

import android.content.Context
import com.simon.data.ktor.ktorHttpClient
import com.simon.data.network.KtorNetworkCall
import com.simon.data.network.base.NetworkCall
import com.simon.data.network.connectivity.Connectivity
import com.simon.data.network.connectivity.ConnectivityImpl
import com.simon.data.network.datasources.character.CharacterDataSource
import com.simon.data.network.datasources.character.CharacterDataSourceImpl
import com.simon.data.network.repository.CharacterRepositoryImpl
import com.simon.domain.features.character.CharacterRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import io.ktor.client.HttpClient
import kotlinx.serialization.json.Json
import javax.inject.Singleton


val json = Json {
    prettyPrint = true
    isLenient = true
    explicitNulls = false
    ignoreUnknownKeys = true
    encodeDefaults = true
}

@Module
@InstallIn(SingletonComponent::class)
object DataModule {

    @Singleton
    @Provides
    fun provideKtorClient(
    ): HttpClient {
        return ktorHttpClient(
        )
    }

    @Singleton
    @Provides
    fun provideConnectivity(
        @ApplicationContext context: Context
    ): Connectivity {
        return ConnectivityImpl(
            context
        )
    }


    @Singleton
    @Provides
    fun provideNetworkCall(
        @ApplicationContext context: Context,
        client: HttpClient,
        connectivity: Connectivity,
    ): NetworkCall {
        return KtorNetworkCall(context, client, connectivity)
    }

    @Singleton
    @Provides
    fun provideCharacterDatasource(
        networkCall: NetworkCall
    ): CharacterDataSource {
        return CharacterDataSourceImpl(networkCall)
    }

    @Singleton
    @Provides
    fun provideCharacterRepository(
        dataSource: CharacterDataSource
    ): CharacterRepository {
        return CharacterRepositoryImpl(dataSource)
    }


}
