package ru.dev4rev.kids.zoobukvy.data.room.entity.card

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import ru.dev4rev.kids.zoobukvy.data.mapper.DataEntity

@Entity(tableName = "letters", indices = [Index(value = ["letter", "face"], unique = true)])
class LetterCardInDatabase(
    @field:ColumnInfo(name = "letter")
    val letter: String,
    @field:ColumnInfo(name = "face")
    val faceImageName: String,
    @field:PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    @field:ColumnInfo(name = "sound")
    val baseSoundName: String,
    @field:ColumnInfo(name = "sound_letter")
    val letterName: String,
    @field:ColumnInfo(name = "soft_sound")
    val softSoundName: String?
) : DataEntity


