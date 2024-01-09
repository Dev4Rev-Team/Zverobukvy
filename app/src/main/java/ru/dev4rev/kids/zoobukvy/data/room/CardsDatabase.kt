package ru.dev4rev.kids.zoobukvy.data.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import ru.dev4rev.kids.zoobukvy.data.room.dao.card.CardsSetDao
import ru.dev4rev.kids.zoobukvy.data.room.dao.card.LetterCardsDao
import ru.dev4rev.kids.zoobukvy.data.room.dao.card.WordCardsDao
import ru.dev4rev.kids.zoobukvy.data.room.entity.card.CardsSetInDatabase
import ru.dev4rev.kids.zoobukvy.data.room.entity.card.LetterCardInDatabase
import ru.dev4rev.kids.zoobukvy.data.room.entity.card.WordCardInDatabase

@Database(
    entities = [
        LetterCardInDatabase::class,
        WordCardInDatabase::class,
        CardsSetInDatabase::class
    ],
    version = 1,
    exportSchema = true
)
abstract class CardsDatabase : RoomDatabase() {

    abstract fun letterCardsDao(): LetterCardsDao

    abstract fun wordCardsDao(): WordCardsDao

    abstract fun cardsSetDao(): CardsSetDao

    companion object {
        private var instance: CardsDatabase? = null

        private const val NAME_CARDS_DATABASE = "cards_db"

        private const val DATABASE_HAS_NOT_CREATED = "Cards database has not created"

        fun getCardsDatabase(): CardsDatabase =
            instance ?: throw RuntimeException(DATABASE_HAS_NOT_CREATED)

        fun createInstanceDatabase(context: Context) {
            if (instance == null) {
                instance = Room.databaseBuilder(
                    context,
                    CardsDatabase::class.java,
                    NAME_CARDS_DATABASE
                )
                    .createFromAsset("database/cards_db.db")
                    .build()
            }
        }

    }
}