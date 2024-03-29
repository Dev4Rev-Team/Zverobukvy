package ru.dev4rev.kids.zoobukvy.data.room.entity.card

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import ru.dev4rev.kids.zoobukvy.data.mapper.DataEntity

@Entity(tableName = "words", indices = [Index(value = ["word", "face"], unique = true)])
class WordCardInDatabase (
    @field:ColumnInfo(name = "word")
    val word: String,
    @field:ColumnInfo(name = "face")
    val faceImageName: String,
    @field:PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    @field:ColumnInfo(name = "sound")
    val soundName: String
): DataEntity