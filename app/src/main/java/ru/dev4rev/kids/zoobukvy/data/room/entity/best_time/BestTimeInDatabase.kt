package ru.dev4rev.kids.zoobukvy.data.room.entity.best_time

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import ru.dev4rev.kids.zoobukvy.data.mapper.DataEntity

@Entity(
    tableName = "best_time"
)
data class BestTimeInDatabase(
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "id_types_cards")
    val idTypesCards: Int,
    @field:ColumnInfo(name = "players_name")
    val playersName: String,
    @field:ColumnInfo(name = "time")
    val time: Long
) : DataEntity
