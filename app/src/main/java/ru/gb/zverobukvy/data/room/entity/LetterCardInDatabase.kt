package ru.gb.zverobukvy.data.room.entity

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "letters", indices = [Index(value = ["letter", "face"], unique = true)])
data class LetterCardInDatabase(
    @field:PrimaryKey
    @field:ColumnInfo(name = "letter")
    val letter: Char,
    @Embedded
    val colorCards: ColorCards,
    @field:ColumnInfo(name = "face")
    val faceImageName: String,
    @field:ColumnInfo(name = "back")
    val backImageName: String
): DataEntity


