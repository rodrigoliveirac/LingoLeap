package com.rodcollab.lingoleap.search

import com.rodcollab.lingoleap.model.Word
import java.util.UUID

class DummyRepository {

    private val list: MutableList<Word> = mutableListOf(
        Word(
            id = UUID.randomUUID().toString(),
            word = "Hello",
            meaning = "Type of greeting"
        ),
        Word(
            id = UUID.randomUUID().toString(),
            word = "Hello",
            meaning = "Type of greeting"
        ),
        Word(
            id = UUID.randomUUID().toString(),
            word = "Hello",
            meaning = "Type of greeting"
        )
    )

    fun getWords() = list.map { it.copy() }

}
