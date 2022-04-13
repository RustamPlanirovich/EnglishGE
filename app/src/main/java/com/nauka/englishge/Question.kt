package com.nauka.englishge

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable



@Entity(tableName = "englisGE")
data class Question(
    @SerializedName("countCorrectAnswers")
    @ColumnInfo(name = "countCorrectAnswers")
    var countCorrectAnswers: Int,
    @SerializedName("countIncorrectAnswers")
    @ColumnInfo(name = "countIncorrectAnswers")
    var countIncorrectAnswers: Int,
    @SerializedName("id")
    @ColumnInfo(name = "id")
    @PrimaryKey
    val id: Int,
    @SerializedName("removeFromStudy")
    @ColumnInfo(name = "removeFromStudy")
    var removeFromStudy: Boolean,
    @SerializedName("sentenceInEnglish")
    @ColumnInfo(name = "sentenceInEnglish")
    var sentenceInEnglish: String,
    @SerializedName("sentenceInRussian")
    @ColumnInfo(name = "sentenceInRussian")
    var sentenceInRussian: String,
    @SerializedName("totalCount")
    @ColumnInfo(name = "totalCount")
    var totalCount: Int,
    @SerializedName("transcription")
    @ColumnInfo(name = "transcription")
    val transcription: String
)

data class WallpaperClickListener(val clickListener: (wallpaper: Question) -> Unit)