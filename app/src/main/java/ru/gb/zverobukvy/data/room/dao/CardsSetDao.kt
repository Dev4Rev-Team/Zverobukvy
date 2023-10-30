package ru.gb.zverobukvy.data.room.dao

import androidx.room.Dao
import androidx.room.Query
import ru.gb.zverobukvy.data.room.entity.CardsSetInDatabase


@Dao
interface CardsSetDao {
    @Query("SELECT * FROM cards_set WHERE color = :color")
    suspend fun cardsSetByColor(color: String): List<CardsSetInDatabase>
}