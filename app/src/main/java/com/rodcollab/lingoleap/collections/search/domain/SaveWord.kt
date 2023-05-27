package com.rodcollab.lingoleap.collections.search.domain

interface SaveWord {

    suspend operator fun invoke(name:String)

}
