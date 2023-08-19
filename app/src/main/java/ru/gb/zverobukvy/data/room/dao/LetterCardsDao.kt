package ru.gb.zverobukvy.data.room.dao

import androidx.room.Dao
import androidx.room.Query
import ru.gb.zverobukvy.data.room.entity.LetterCardInDatabase

@Dao
interface LetterCardsDao {
    @Query("SELECT * FROM letters")
    suspend fun getLetterCards(): List<LetterCardInDatabase>
}