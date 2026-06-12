package com.vluk4.itunescodechallenge.core.network.di

import com.vluk4.itunescodechallenge.core.network.ItunesNetworkDataSource
import com.vluk4.itunescodechallenge.core.network.internal.ItunesApiService
import com.vluk4.itunescodechallenge.core.network.internal.ItunesNetworkDataSourceImpl
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal object NetworkProvidesModule {

    private const val BASE_URL = "https://itunes.apple.com/"

    @Provides
    @Singleton
    fun provideJson(): Json = Json {
        ignoreUnknownKeys = true
        coerceInputValues = true
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient = OkHttpClient.Builder()
        .addInterceptor(
            HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BASIC }
        )
        .build()

    @Provides
    @Singleton
    fun provideRetrofit(json: Json, client: OkHttpClient): Retrofit {
        val contentType = "application/json".toMediaType()
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(json.asConverterFactory(contentType))
            .build()
    }

    @Provides
    @Singleton
    fun provideApiService(retrofit: Retrofit): ItunesApiService =
        retrofit.create(ItunesApiService::class.java)
}

@Module
@InstallIn(SingletonComponent::class)
internal abstract class NetworkBindsModule {

    @Binds
    @Singleton
    abstract fun bindNetworkDataSource(
        impl: ItunesNetworkDataSourceImpl,
    ): ItunesNetworkDataSource
}
