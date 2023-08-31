package ru.gb.zverobukvy.data.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import ru.gb.zverobukvy.data.room.dao.AvatarsDao
import ru.gb.zverobukvy.data.room.dao.LetterCardsDao
import ru.gb.zverobukvy.data.room.dao.PlayersDao
import ru.gb.zverobukvy.data.room.dao.WordCardsDao
import ru.gb.zverobukvy.data.room.entity.AvatarInDatabase
import ru.gb.zverobukvy.data.room.entity.LetterCardInDatabase
import ru.gb.zverobukvy.data.room.entity.PlayerInDatabase
import ru.gb.zverobukvy.data.room.entity.WordCardInDatabase

@Database(
    entities = [
        PlayerInDatabase::class,
        AvatarInDatabase::class,
        LetterCardInDatabase::class,
        WordCardInDatabase::class
    ],
    version = 1,
    exportSchema = false
)
abstract class AnimalLettersDatabase : RoomDatabase() {
    abstract fun playersDao(): PlayersDao

    abstract fun letterCardsDao(): LetterCardsDao

    abstract fun wordCardsDao(): WordCardsDao

    abstract fun avatarsDao(): AvatarsDao

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