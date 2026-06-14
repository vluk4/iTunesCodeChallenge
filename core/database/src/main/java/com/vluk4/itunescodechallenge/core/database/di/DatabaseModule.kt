package com.vluk4.itunescodechallenge.core.database.di

import android.content.Context
import androidx.room.Room
import com.vluk4.itunescodechallenge.core.database.ItunesDatabase
import com.vluk4.itunescodechallenge.core.database.dao.SongDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): ItunesDatabase =
        Room.databaseBuilder(context, ItunesDatabase::class.java, "itunes.db")
            .fallbackToDestructiveMigration(dropAllTables = true)
            .build()

    @Provides
    fun provideSongDao(database: ItunesDatabase): SongDao = database.songDao()
}
