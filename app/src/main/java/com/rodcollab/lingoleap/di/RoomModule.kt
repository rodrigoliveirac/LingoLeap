package com.rodcollab.lingoleap.di

import android.app.Application
import com.rodcollab.lingoleap.core.database.AppDatabase
import com.rodcollab.lingoleap.core.database.SearchHistoryDao
import com.rodcollab.lingoleap.core.database.SongDao
import com.rodcollab.lingoleap.features.word.translation.LanguagesDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object RoomModule {

    @Singleton
    @Provides
    fun providesRoomDatabase(application: Application)  : AppDatabase {
        return AppDatabase.getInstance(application)
    }

    @Singleton
    @Provides
    fun providesSearchHistoryDao(database: AppDatabase) : SearchHistoryDao {
        return database.searchHistoryDao()
    }

    @Singleton
    @Provides
    fun providesLanguagesDao(database: AppDatabase) : LanguagesDao {
        return database.languageDao()
    }

    @Singleton
    @Provides
    fun providesSongsDao(database: AppDatabase) : SongDao {
        return database.songDao()
    }
}