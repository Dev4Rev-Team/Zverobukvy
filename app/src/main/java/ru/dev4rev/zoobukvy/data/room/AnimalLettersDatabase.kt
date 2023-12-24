package ru.dev4rev.zoobukvy.data.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import ru.dev4rev.zoobukvy.data.room.dao.player.AvatarsDao
import ru.dev4rev.zoobukvy.data.room.dao.card.CardsSetDao
import ru.dev4rev.zoobukvy.data.room.dao.card.LetterCardsDao
import ru.dev4rev.zoobukvy.data.room.dao.player.PlayersDao
import ru.dev4rev.zoobukvy.data.room.dao.card.WordCardsDao
import ru.dev4rev.zoobukvy.data.room.entity.player.AvatarInDatabase
import ru.dev4rev.zoobukvy.data.room.entity.card.CardsSetInDatabase
import ru.dev4rev.zoobukvy.data.room.entity.card.LetterCardInDatabase
import ru.dev4rev.zoobukvy.data.room.entity.player.PlayerInDatabase
import ru.dev4rev.zoobukvy.data.room.entity.card.WordCardInDatabase

@Database(
    entities = [
        PlayerInDatabase::class,
        AvatarInDatabase::class,
        LetterCardInDatabase::class,
        WordCardInDatabase::class,
        CardsSetInDatabase::class
    ],
    version = 1,
    exportSchema = true
)
abstract class AnimalLettersDatabase : RoomDatabase() {
    abstract fun playersDao(): PlayersDao

    abstract fun letterCardsDao(): LetterCardsDao

    abstract fun wordCardsDao(): WordCardsDao

    abstract fun avatarsDao(): AvatarsDao

    abstract fun cardsSetDao(): CardsSetDao

    companion object {
        private var instance: AnimalLettersDatabase? = null

        private const val NAME_PLAYERS_DATABASE = "animal_letters_db"

        private const val DATABASE_HAS_NOT_CREATED = "Database has not created"

        fun getPlayersDatabase(): AnimalLettersDatabase =
            instance ?: throw RuntimeException(DATABASE_HAS_NOT_CREATED)

        fun createInstanceDatabase(context: Context) {
            if (instance == null) {
                instance = Room.databaseBuilder(
                    context,
                    AnimalLettersDatabase::class.java,
                    NAME_PLAYERS_DATABASE
                )
                    .createFromAsset("database/animal_letters_db.db")
                    .build()
            }
        }
    }
}