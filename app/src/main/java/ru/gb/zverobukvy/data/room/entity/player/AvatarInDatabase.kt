package ru.gb.zverobukvy.data.room.entity.player

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import ru.gb.zverobukvy.data.mapper.DataEntity

@Entity (tableName = "avatars", indices = [Index(value = ["id", "image_name"], unique = true)])
class AvatarInDatabase(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    @field:ColumnInfo(name = "image_name")
    val imageName: String,
    @field:ColumnInfo(name = "is_standard")
    val isStandard: Boolean
) : DataEntity