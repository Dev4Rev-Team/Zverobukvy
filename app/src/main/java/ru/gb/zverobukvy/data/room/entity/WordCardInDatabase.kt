package ru.gb.zverobukvy.data.room.entity

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import ru.gb.zverobukvy.data.mapper.DataEntity

@Entity(tableName = "words", indices = [Index(value = ["word", "face"], unique = true)])
class WordCardInDatabase (
    @field:ColumnInfo(name = "word")
    val word: String,
    @Embedded
    val typeCardsInDatabase: TypeCardsInDatabase,
    @field:ColumnInfo(name = "face")
    val faceImageName: String,
    @field:PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    @field:ColumnInfo(name = "sound")
    val soundName: String
): DataEntity