package ru.gb.zverobukvy.data.room.dao

import androidx.room.Dao
import androidx.room.Query
import ru.gb.zverobukvy.data.room.entity.AvatarInDatabase

@Dao
interface AvatarsDao {
    @Query("SELECT * FROM avatars")
    suspend fun getAvatars(): List<AvatarInDatabase>
}