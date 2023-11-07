package ru.gb.zverobukvy.data.room.dao.card

import androidx.room.Dao
import androidx.room.Query
import ru.gb.zverobukvy.data.room.entity.card.LetterCardInDatabase

@Dao
interface LetterCardsDao {
    @Query("SELECT * FROM letters")
    suspend fun getLetterCards(): List<LetterCardInDatabase>
}