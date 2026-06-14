package com.vluk4.itunescodechallenge.core.player.di

import com.vluk4.itunescodechallenge.core.domain.player.AudioPlayer
import com.vluk4.itunescodechallenge.core.player.ExoAudioPlayer
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal abstract class AudioPlayerModule {

    @Binds
    @Singleton
    internal abstract fun bindAudioPlayer(impl: ExoAudioPlayer): AudioPlayer
}
