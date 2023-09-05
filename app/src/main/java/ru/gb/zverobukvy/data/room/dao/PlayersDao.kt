package ru.gb.zverobukvy.data.room.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import ru.gb.zverobukvy.data.room.entity.PlayerInDatabase
import ru.gb.zverobukvy.data.room.entity.PlayerWithAvatar

@Dao
interface PlayersDao {
    @Transaction
    @Query("SELECT * FROM players, avatars WHERE players.id_avatar=avatars.id")
    suspend fun getPlayers(): List<PlayerWithAvatar>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPlayer(player: PlayerInDatabase): Long

    @Delete
    suspend fun deletePlayer(player: PlayerInDatabase)

    @Update
    suspend fun updatePlayer(player: PlayerInDatabase)
}