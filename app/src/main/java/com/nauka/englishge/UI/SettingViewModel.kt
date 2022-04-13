package com.nauka.englishge.UI

import android.content.ClipData
import android.content.Context
import android.net.Uri
import androidx.lifecycle.*
import androidx.paging.*
import com.google.gson.Gson
import com.nauka.englishge.Question
import com.nauka.englishge.Questions
import com.nauka.englishge.Repository
import com.nauka.englishge.room.AppDatabase
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.coroutines.SuspendFunction1

@HiltViewModel
class SettingViewModel @Inject constructor(
    @ApplicationContext val context: Context,
    private var repository: Repository
) : ViewModel() {

     val mutableSelectedItem = MutableLiveData<Question>()





    val characters: Flow<PagingData<Question>> =
        Pager(config = PagingConfig(pageSize = 10, prefetchDistance = 2),
            pagingSourceFactory = { repository.getDatabase().getAllPaged() }
        ).flow.cachedIn(viewModelScope)

    fun charactersSeartch(text:String): Flow<PagingData<Question>> =
        Pager(config = PagingConfig(pageSize = 10, prefetchDistance = 2),
            pagingSourceFactory = {
                repository.getDatabase().getById(text)
            }
        ).flow.cachedIn(viewModelScope)



    fun readDataFromJsonFile(path: Uri?) {
        val jsonSelectedFile = context.contentResolver.openInputStream(path!!);
        val inputAsString = jsonSelectedFile?.bufferedReader().use { it?.readText() }
        val gson = Gson()
        val data = gson.fromJson(inputAsString, Questions::class.java)

        for (quest in data.questions) {
            viewModelScope.launch(Dispatchers.IO) {
                if (quest != null) {
                    repository.getDatabase().insertAll(quest)
                }
            }
        }
    }





}