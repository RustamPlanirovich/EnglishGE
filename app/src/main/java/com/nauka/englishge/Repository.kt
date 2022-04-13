package com.nauka.englishge

import com.nauka.englishge.room.AppDatabase
import javax.inject.Inject

class Repository @Inject constructor(private val db: AppDatabase) {
    fun getDatabase() = db.dao()
}