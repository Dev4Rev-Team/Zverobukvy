package ru.dev4rev.kids.zoobukvy.data.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import ru.dev4rev.kids.zoobukvy.data.room.AnimalLettersDatabaseMigration.Companion.MIGRATION_1_2
import ru.dev4rev.kids.zoobukvy.data.room.AnimalLettersDatabaseMigration.Companion.MIGRATION_2_3
import ru.dev4rev.kids.zoobukvy.data.room.dao.best_time.BestTimeDao
import ru.dev4rev.kids.zoobukvy.data.room.dao.player.AvatarsDao
import ru.dev4rev.kids.zoobukvy.data.room.dao.player.PlayersDao
import ru.dev4rev.kids.zoobukvy.data.room.entity.best_time.BestTimeInDatabase
import ru.dev4rev.kids.zoobukvy.data.room.entity.player.AvatarInDatabase
import ru.dev4rev.kids.zoobukvy.data.room.entity.player.PlayerInDatabase

@Database(
    entities = [
        PlayerInDatabase::class,
        AvatarInDatabase::class,
        BestTimeInDatabase::class
    ],
    version = 3,
    exportSchema = true
)
abstract class AnimalLettersDatabase : RoomDatabase() {
    abstract fun playersDao(): PlayersDao

    abstract fun avatarsDao(): AvatarsDao

    abstract fun bestTimeDao(): BestTimeDao

    companion object {
        private var instance: AnimalLettersDatabase? = null

        private const val NAME_PLAYERS_DATABASE = "animal_letters_db"

        private const val DATABASE_HAS_NOT_CREATED = "Players database has not created"

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
                    .addMigrations(MIGRATION_1_2, MIGRATION_2_3)
                    .build()
            }
        }
    }
}