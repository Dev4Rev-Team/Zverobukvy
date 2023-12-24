package ru.dev4rev.kids.zoobukvy.data.room.dao.card

import androidx.room.Dao
import androidx.room.Query
import ru.dev4rev.kids.zoobukvy.data.room.entity.card.CardsSetInDatabase


@Dao
interface CardsSetDao {
    @Query("SELECT * FROM cards_set WHERE color = :color")
    suspend fun getCardsSetByColor(color: String): List<CardsSetInDatabase>
}