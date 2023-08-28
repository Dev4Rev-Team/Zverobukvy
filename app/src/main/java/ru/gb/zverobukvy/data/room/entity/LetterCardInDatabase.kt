package ru.gb.zverobukvy.data.room.entity

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import ru.gb.zverobukvy.data.mapper.DataEntity

@Entity(tableName = "letters", indices = [Index(value = ["letter", "face"], unique = true)])
data class LetterCardInDatabase(
    @field:ColumnInfo(name = "letter")
    val letter: String,
    @Embedded
    val typeCardsInDatabase: TypeCardsInDatabase,
    @field:ColumnInfo(name = "face")
    val faceImageName: String,
    @field:ColumnInfo(name = "back")
    val backImageName: String,
    @field:PrimaryKey(autoGenerate = true)
    val id: Long = 0
) : DataEntity


