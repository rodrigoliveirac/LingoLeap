package com.rodcollab.lingoleap.core.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.rodcollab.lingoleap.core.networking.dictionary.model.DefinitionEntity
import com.rodcollab.lingoleap.core.networking.dictionary.model.MeaningEntity
import com.rodcollab.lingoleap.core.networking.dictionary.model.WordEntity
import com.rodcollab.lingoleap.features.word.detail.SongEntity
import com.rodcollab.lingoleap.features.word.translation.LanguageEntity
import com.rodcollab.lingoleap.features.word.translation.LanguagesDao

@Database(entities = [SongEntity::class, WordEntity::class, MeaningEntity::class, DefinitionEntity::class, LanguageEntity::class], version = 10, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    abstract fun searchHistoryDao() : SearchHistoryDao
    abstract fun languageDao() : LanguagesDao
    abstract fun songDao() : SongDao

    companion object {
        private var instance: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            if (instance == null) {
                synchronized(AppDatabase::class) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        AppDatabase::class.java,
                        DATABASE_NAME
                    ).fallbackToDestructiveMigration().build()
                }
            }
            return instance!!
        }

        private const val DATABASE_NAME = "app-database.db"
    }
}