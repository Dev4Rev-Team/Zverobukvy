package ru.dev4rev.kids.zoobukvy.data.room.entity.player

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import ru.dev4rev.kids.zoobukvy.data.mapper.DataEntity
import ru.dev4rev.kids.zoobukvy.domain.entity.player.LettersGuessingLevel
import ru.dev4rev.kids.zoobukvy.domain.entity.player.Rating

@Entity(
    tableName = "players", foreignKeys = [ForeignKey(
        entity = AvatarInDatabase::class,
        parentColumns = ["id"],
        childColumns = ["id_avatar"],
        onDelete = ForeignKey.SET_DEFAULT
    )]
)
class PlayerInDatabase(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id_player")
    val idPlayer: Long = 0,
    @field:ColumnInfo(name = "name")
    var name: String,
    @field:ColumnInfo(name = "id_avatar", defaultValue = "1")
    val idAvatar: Long,
    @Embedded
    val rating: Rating,
    @Embedded
    var lettersGuessingLevel: LettersGuessingLevel
) : DataEntity
