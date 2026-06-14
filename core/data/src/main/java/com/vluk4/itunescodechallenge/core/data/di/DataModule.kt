package com.vluk4.itunescodechallenge.core.data.di

import com.vluk4.itunescodechallenge.core.common.dispatcher.DefaultDispatcherProvider
import com.vluk4.itunescodechallenge.core.common.dispatcher.DispatcherProvider
import com.vluk4.itunescodechallenge.core.data.repository.SongRepositoryImpl
import com.vluk4.itunescodechallenge.core.domain.repository.SongRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class DataModule {

    @Binds
    @Singleton
    internal abstract fun bindSongRepository(impl: SongRepositoryImpl): SongRepository

    companion object {
        @Provides
        @Singleton
        fun provideDispatcherProvider(): DispatcherProvider = DefaultDispatcherProvider()
    }
}
