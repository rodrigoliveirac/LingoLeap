package com.rodcollab.lingoleap.search

interface SaveWord {

    suspend operator fun invoke(name:String)

}
