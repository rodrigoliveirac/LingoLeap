package com.rodcollab.lingoleap.features.word.practice

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rodcollab.lingoleap.WordDetailsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.UUID
import javax.inject.Inject

sealed class Resource<out T : Any> {
    data class Success<out T : Any>(val data: T) : Resource<T>()
    data class Error(val exception: Throwable) : Resource<Nothing>()
    object Loading : Resource<Nothing>()

    fun toData(): T? = if (this is Success) this.data else null
}

data class PracticeUiState(
    val question: List<List<WordItem>> = listOf(),
    val answer: List<List<WordItem>> = listOf()
)

@HiltViewModel
class PracticeViewModel @Inject constructor(
    private val gameManager: GameManager,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val word = savedStateHandle.get<String>("word").orEmpty()

    private var _uiState = MutableStateFlow(PracticeUiState())
    val uiState = _uiState.asStateFlow()

    init {
        getQuestion()
    }

    private fun getQuestion() {
        viewModelScope.launch {
            _uiState.update {
                PracticeUiState(question = gameManager(word).chunked(4), answer = gameManager.getAnswer().chunked(5))
            }
        }
    }

    fun dragItem(index: Int) {
        viewModelScope.launch {
            /*gameManager.dragItem(index)
            _uiState.update {
                PracticeUiState(question = gameManager.getRandomWords().chunked(4))
            }*/
        }
    }
    fun dropItem(id: String, value: String) {
        viewModelScope.launch {
            gameManager.dropItem(id, value)
           /* _uiState.update {
               // PracticeUiState(question = it.question, answer = gameManager.getAnswer().)
            }*/
        }
    }
}

interface GameManager {

    suspend operator fun invoke(word: String): List<WordItem>
    suspend fun getRandomWords(): List<WordItem>
    suspend fun dragItem(index: Int)
    suspend fun dropItem(id: String, value: String)
    suspend fun getAnswer() : List<WordItem>
}

class GameManagerImpl @Inject constructor(private val question: GetQuestion) : GameManager {

    private val randomWords = mutableListOf<WordItem>()
    private val flowRandomWords = MutableStateFlow(randomWords)

    private val answer = mutableListOf<WordItem>()
    private val flowAnswer = MutableStateFlow(answer)

    override suspend fun invoke(word: String): List<WordItem> {
        generateQuestion(word)
        return flowRandomWords.value
    }

    private suspend fun generateQuestion(word: String) {

        val question = question(word)

        withContext(Dispatchers.IO) {
            question.randomWords.map { item ->
                randomWords.add(item)
            }
            flowRandomWords.value = randomWords
        }

        withContext(Dispatchers.IO) {
            question.answer.map { item ->
                answer.add(item)
            }
            flowAnswer.value = answer
        }
    }

    override suspend fun getRandomWords(): List<WordItem> = flowRandomWords.value

    override suspend fun dragItem(index: Int) {
        randomWords.removeAt(index)
    }

    override suspend fun dropItem(id: String, value:String) {
        answer.add(WordItem(id,value))
        flowAnswer.value = answer
    }

    override suspend fun getAnswer(): List<WordItem> = flowAnswer.value

}

interface GetQuestion {

    suspend operator fun invoke(word: String): Question
}

class GetQuestionImpl @Inject constructor(private val repository: WordDetailsRepository) :
    GetQuestion {

    override suspend operator fun invoke(word: String): Question {

        var question: Question? = null

        getExample(word) { example ->

            question = withContext(Dispatchers.IO) {

                val correctOrder = example.split(" ").map { value ->
                    val id = withContext(Dispatchers.IO) {
                        UUID.randomUUID().toString()
                    }
                    WordItem(
                        id = id,
                        value = value
                    )
                }
                var randomWords = correctOrder.toMutableList()
                while (correctOrder == randomWords) {
                    randomWords.clear()
                    randomWords = correctOrder.shuffled().toMutableList()
                    break
                }

                val words = randomWords.map { word -> WordItem(id = word.id, value = word.value) }

                Question(answer = correctOrder, randomWords = words)
            }

        }

        return question as Question
    }

    private suspend fun getExample(word: String, successfully: suspend (String) -> Unit) {
        withContext(Dispatchers.IO) {
            val example =
                repository.getAllMeaningsFromWord(word).filter { it.example != "" }.random().example
            successfully(example)
        }
    }
}

data class Question(val answer: List<WordItem>, val  randomWords: List<WordItem>)

data class WordItem(val id: String, val value: String)