package ru.gb.zverobukvy.domain.entity

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "players", indices = [Index(value = ["name"], unique = true)])
data class Player(
    @field:ColumnInfo(name = "name")
    var name: String,
    @field:PrimaryKey(autoGenerate = true)
    val id: Long = 0
    ): Parcelable
