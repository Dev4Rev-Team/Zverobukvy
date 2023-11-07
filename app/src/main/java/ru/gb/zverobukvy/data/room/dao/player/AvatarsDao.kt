package ru.gb.zverobukvy.data.room.dao.player

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import ru.gb.zverobukvy.data.room.entity.player.AvatarInDatabase

@Dao
interface AvatarsDao {
    @Query("SELECT * FROM avatars")
    suspend fun getAvatars(): List<AvatarInDatabase>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAvatar(avatar: AvatarInDatabase): Long
}