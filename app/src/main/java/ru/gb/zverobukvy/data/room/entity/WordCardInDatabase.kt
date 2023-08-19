package ru.gb.zverobukvy.data.room.entity

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "words", indices = [Index(value = ["word", "face"], unique = true)])
class WordCardInDatabase (
    @field:PrimaryKey
    @field:ColumnInfo(name = "word")
    val word: String,
    @Embedded
    val colorCards: ColorCards,
    @field:ColumnInfo(name = "face")
    val faceImageName: String
): DataEntity