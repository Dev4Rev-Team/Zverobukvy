package ru.gb.zverobukvy.data.room.dao.card

import androidx.room.Dao
import androidx.room.Query
import ru.gb.zverobukvy.data.room.entity.card.WordCardInDatabase

@Dao
interface WordCardsDao {
    @Query("SELECT * FROM words")
    suspend fun getWordCards(): List<WordCardInDatabase>
}