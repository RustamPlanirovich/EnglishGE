package com.nauka.englishge

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "englisGE")
data class Questions(
    @PrimaryKey
    @SerializedName("questions")
    val questions: List<Question>
)