package com.rodcollab.lingoleap.core.networking.dictionary.model

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
)

@Entity(tableName = "meaning")
data class MeaningEntity(
    @PrimaryKey
    val meaningId: String,
    val wordCreatorId: String,
    val partOfSpeech: String
)

data class WordWithMeanings(
    @Embedded val word: WordEntity,
    @Relation(
        parentColumn = "word",
        entityColumn = "wordCreatorId"
    )
    val meanings: List<MeaningEntity>
)

@Entity(tableName = "definition")
data class DefinitionEntity(
    @PrimaryKey
    val definitionId: String,
    val meaningCreatorId: String,
    val definition: String,
    val example: String
)

data class MeaningWithDefinitions(
    @Embedded val meaning: MeaningEntity,
    @Relation(
        parentColumn = "meaningId",
        entityColumn = "meaningCreatorId"
    )
    val definitions: List<DefinitionEntity>
)