package com.rodcollab.lingoleap.di

import android.app.Application
import com.rodcollab.lingoleap.core.database.AppDatabase
import com.rodcollab.lingoleap.core.database.SearchHistoryDao
import com.rodcollab.lingoleap.core.database.dao.SavedWordDao
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
    fun providesSavedWordDao(database: AppDatabase) : SavedWordDao {
        return database.savedWordDao()
    }

    @Singleton
    @Provides
    fun providesSearchHistoryDao(database: AppDatabase) : SearchHistoryDao {
        return database.searchHistoryDao()
    }
}