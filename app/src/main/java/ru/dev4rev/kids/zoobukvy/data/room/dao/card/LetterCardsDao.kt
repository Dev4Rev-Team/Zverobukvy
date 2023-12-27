package ru.dev4rev.kids.zoobukvy.data.room.dao.card

import androidx.room.Dao
import androidx.room.Query
import ru.dev4rev.kids.zoobukvy.data.room.entity.card.LetterCardInDatabase

@Dao
interface LetterCardsDao {
    @Query("SELECT * FROM letters")
    suspend fun getLetterCards(): List<LetterCardInDatabase>
}