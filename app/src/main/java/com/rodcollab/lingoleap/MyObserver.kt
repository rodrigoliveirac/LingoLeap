package com.rodcollab.lingoleap

import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import com.rodcollab.lingoleap.profile.SavedViewModel
import com.rodcollab.lingoleap.search.SearchViewModel

class MyObserver(private val savedViewModel: SavedViewModel, private val viewModel: SearchViewModel) : DefaultLifecycleObserver {
    override fun onResume(owner: LifecycleOwner) {
        super.onResume(owner)
        viewModel.onResume()
        savedViewModel.showList()
    }
}