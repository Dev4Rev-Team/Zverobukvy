package ru.dev4rev.kids.zoobukvy.data.room.dao.player

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import ru.dev4rev.kids.zoobukvy.data.room.entity.player.PlayerInDatabase
import ru.dev4rev.kids.zoobukvy.data.room.entity.player.PlayerWithAvatar

@Dao
interface PlayersDao {
    @Transaction
    @Query("SELECT * FROM players, avatars WHERE players.id_avatar=avatars.id")
    suspend fun getPlayers(): List<PlayerWithAvatar>

    @Query("SELECT players.name FROM players WHERE players.id_player=:playersId")
    suspend fun getPlayersNameById(playersId: Long): String?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPlayer(player: PlayerInDatabase): Long

    @Delete
    suspend fun deletePlayer(player: PlayerInDatabase)

    @Update
    suspend fun updatePlayer(player: PlayerInDatabase)
}