package com.nauka.englishge.room

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

import com.nauka.englishge.Question
import com.nauka.englishge.Questions

@Database(entities = [Question::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun dao(): Dao
}