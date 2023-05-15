package com.rodcollab.lingoleap.search

import com.rodcollab.lingoleap.model.Word
import java.util.UUID

class DummyRepository {

    private val list: MutableList<Word> = mutableListOf(
        Word(
            id = UUID.randomUUID().toString(),
            word = "Hello",
            meaning = "Type of greeting",
            audio = "https://ssl.gstatic.com/dictionary/static/sounds/20200429/hello--_gb_1.mp3"
        ),
        Word(
            id = UUID.randomUUID().toString(),
            word = "Hello",
            meaning = "Type of greeting",
            audio = "https://ssl.gstatic.com/dictionary/static/sounds/20200429/hello--_gb_1.mp3"
        ),
        Word(
            id = UUID.randomUUID().toString(),
            word = "Hello",
            meaning = "Type of greeting",
            audio = "https://ssl.gstatic.com/dictionary/static/sounds/20200429/hello--_gb_1.mp3"
        )
    )

    fun getWords() = list.map { it.copy() }

}
