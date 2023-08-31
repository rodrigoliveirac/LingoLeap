package com.rodcollab.lingoleap.features.word.translation

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.rodcollab.lingoleap.core.networking.translator.TranslatorApiService
import com.rodcollab.lingoleap.features.word.detail.LanguageOption
import kotlinx.coroutines.*
import javax.inject.Inject

interface TranslationRepository {

    suspend fun translate(targetLanguage: String, text: String): String
    suspend fun fetchLanguagesFromLocal(): List<LanguageOption>
    suspend fun fetchLanguagesFromApi(): List<LanguageOption>
    suspend fun insertToLocal(language: LanguageOption)

}

class TranslationRepositoryImpl @Inject constructor(
    private val dao: LanguagesDao,
    private val translationService: TranslatorApiService,
) : TranslationRepository {
    override suspend fun translate(targetLanguage: String, text: String): String {
        val translatedText = withContext(Dispatchers.IO) {
            translationService.translate(targetLanguage, "en", text)
        }
        if (translatedText.isSuccessful) {
            return translatedText.body()!!.data.translatedText
        }
        return ""
    }


    override suspend fun fetchLanguagesFromLocal(): List<LanguageOption> = dao.fetchAll().map {
        LanguageOption(
            code = it.code,
            name = it.name
        )
    }

    override suspend fun fetchLanguagesFromApi(): List<LanguageOption> =
        translationService.getLanguages().body()!!.data.languages.map {
            LanguageOption(
                code = it.code,
                name = it.name
            )
        }

    override suspend fun insertToLocal(language: LanguageOption) {
        dao.insert(language.toLanguageEntity(language))
    }

}

fun LanguageOption.toLanguageEntity(language: LanguageOption): LanguageEntity =
    LanguageEntity(
        code = language.code,
        name = language.name
    )


@Entity(tableName = "language")
data class LanguageEntity(
    @PrimaryKey val code: String,
    @ColumnInfo("name") val name: String
)