package ru.dev4rev.kids.zoobukvy.data.room.dao.best_time

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import ru.dev4rev.kids.zoobukvy.data.room.entity.best_time.BestTimeInDatabase

@Dao
interface BestTimeDao {
    @Query("SELECT * FROM best_time WHERE id_types_cards=:typesCardsId")
    suspend fun getBestTimeById(typesCardsId: Int): BestTimeInDatabase?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBestTime(bestTime: BestTimeInDatabase)

    @Query ("UPDATE best_time SET players_name=:newPlayersName WHERE players_name = :oldPlayersName")
    suspend fun updatePlayersNameById(oldPlayersName: String, newPlayersName: String)
}