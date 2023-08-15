package com.rodcollab.lingoleap

import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

interface TranslationRepository {

    suspend fun loadLanguages(successfully: (List<LanguageOption>) -> Unit)

}

class TranslationRepositoryImpl @Inject constructor(
    private val translationService: TranslatorApiService,
    private val firebase: FirebaseFirestore,
) : TranslationRepository {

    private val _languagesCache = mutableListOf<LanguageOption>()

    private val _languagesFlow = MutableStateFlow(_languagesCache)

    override suspend fun loadLanguages(successfully: (List<LanguageOption>) -> Unit) {

        withContext(Dispatchers.IO) {
            val result =
                firebase.collection("SUPPORTED_LANGUAGES").get().addOnSuccessListener { documents ->
                }.await()
            if (result.isEmpty) {
                addFromApiMock {
                    addToCollection {
                        successfully(_languagesFlow.value)
                    }
                }
            } else {
                fetchLanguages {
                    successfully(it.sortedBy { option -> option.name })
                }
            }
        }
    }

    private suspend fun addToCollection(finished: () -> Unit) {
        withContext(Dispatchers.IO) {
            _languagesFlow.value.map {
                firebase.collection("SUPPORTED_LANGUAGES").add(it)
            }
            finished()
        }
    }

    private suspend fun addFromApiMock(finished: suspend () -> Unit) {

        withContext(Dispatchers.IO) {
            val result = translationService.getLanguages()
            if (result.isSuccessful) {
                result.body()?.data?.languages?.map {
                    _languagesCache.add(LanguageOption(code = it.code, name = it.name))
                    _languagesFlow.value = _languagesCache
                }
                finished()
            }
        }
    }

    private suspend fun fetchLanguages(successfully: (List<LanguageOption>) -> Unit) {
        withContext(Dispatchers.IO) {

            if (_languagesCache.isEmpty()) {
                val result =
                    firebase.collection("SUPPORTED_LANGUAGES").get().addOnSuccessListener { docs ->

                    }.await()


                result.map {
                    _languagesCache.add(
                        LanguageOption(
                            code = it.getString("code")!!,
                            name = it.getString("name")!!
                        )
                    )
                    _languagesFlow.value
                }
                successfully(_languagesFlow.value)
            } else {
                successfully(_languagesFlow.value)
            }
        }
    }

}
