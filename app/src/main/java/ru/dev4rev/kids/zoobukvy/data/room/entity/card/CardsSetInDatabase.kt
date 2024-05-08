package ru.dev4rev.kids.zoobukvy.data.room.entity.card

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import ru.dev4rev.kids.zoobukvy.data.mapper.DataEntity

@Entity(tableName = "cards_set", indices = [Index(value = ["letters"], unique = true)])
class CardsSetInDatabase(
    @field:ColumnInfo(name = "color")
    val color: String,
    @PrimaryKey
    @ColumnInfo(name = "letters")
    val letters: String,
    @field:ColumnInfo(name = "words")
    val words: String
): DataEntity
