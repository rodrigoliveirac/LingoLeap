package com.rodcollab.lingoleap.core.networking.dictionary.model

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Relation
import com.squareup.moshi.Json

data class Definition(
    @Json(name = "antonyms")
    val antonyms: List<Any?>,
    @Json(name = "definition")
    val definition: String?,
    @Json(name = "example")
    val example: String?,
    @Json(name = "synonyms")
    val synonyms: List<Any?>
)

@Entity(tableName = "word")
data class WordEntity(
    @PrimaryKey val word: String,
    val audio: String,
    val marked: Int = 0
)

@Entity(tableName = "meaning")
data class MeaningEntity(
    @PrimaryKey val meaningId: String,
    @ColumnInfo("wordCreatorId")val wordCreatorId: String?,
    @ColumnInfo("partOfSpeech")val partOfSpeech: String?
)

@Entity(tableName = "definition")
data class DefinitionEntity(
    @PrimaryKey
    val definitionId: String,
    val word: String,
    val partOfSpeechCreator: String,
    val definition: String,
    val example: String
)

data class MeaningWithDefinitions(
    @Embedded val meaning: MeaningEntity,
    @Relation(
        parentColumn = "partOfSpeech",
        entityColumn = "partOfSpeechCreator"
    )
    val definitions: List<DefinitionEntity>
)