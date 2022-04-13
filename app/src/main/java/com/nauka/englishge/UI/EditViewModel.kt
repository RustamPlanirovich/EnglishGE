package com.nauka.englishge.UI

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewModelScope
import com.nauka.englishge.Question
import com.nauka.englishge.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EditViewModel @Inject constructor(
    @ApplicationContext val context: Context,
    private var repository: Repository
) : ViewModel() {


    fun update(question: Question) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.getDatabase().update(question)
        }
    }

}