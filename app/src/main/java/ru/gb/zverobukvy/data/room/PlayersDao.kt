package ru.gb.zverobukvy.data.room

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import ru.gb.zverobukvy.domain.entity.Player

@Dao
interface PlayersDao {

    @Query("SELECT * FROM players")
    suspend fun getPlayers(): List<Player>

    @Insert (onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPlayer(player: Player)

    @Insert (onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPlayers(players: List<Player>)

    @Delete
    suspend fun deletePlayer(player: Player)

    @Update
    suspend fun updatePlayer (player: Player)

}