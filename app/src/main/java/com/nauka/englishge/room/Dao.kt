package com.nauka.englishge.room

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.nauka.englishge.Question


@Dao
interface Dao {

    @Query("select * from englisGE where removeFromStudy == 0")
    fun getSentenceStudy(): List<Question>

    @Query("select * from englisGE")
    fun getSentence(): List<Question>

    @Query("SELECT * FROM englisGE WHERE sentenceInEnglish =:text")
    fun getById(text: String): PagingSource<Int, Question>

    @Query("SELECT * FROM englisGE")
    fun getAllPaged(): PagingSource<Int, Question>

    @Insert
    fun insertAll(questions: Question)

    @Update
    fun update(questions: Question)


}