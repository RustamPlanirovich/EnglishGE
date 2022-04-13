package com.nauka.englishge.UI

import android.os.CountDownTimer
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nauka.englishge.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(private var repository: Repository) : ViewModel() {

    var commonData = MutableLiveData<List<Question>>()
    val mutableSelectedItem = MutableLiveData<Question>()

    private val _viewState = MutableStateFlow<LoginUIState>(LoginUIState.Empty)
    val viewUIState: StateFlow<LoginUIState> = _viewState
    private var coun: Int = 10

    init {
        loadDataFromDatabase()
    }

    fun loadDataFromDatabase() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.getDatabase().getSentenceStudy().let {

                commonData.postValue(it)
            }
        }
    }

    fun update(question: Question) {
        repository.getDatabase().update(question)
    }


    fun startTimeCounter() {
        coun = 10
        object : CountDownTimer(11000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                _viewState.value = LoginUIState.Loading("$coun")
                coun--
            }
            override fun onFinish() {
                _viewState.value = LoginUIState.Success
            }
        }.start()
    }
}