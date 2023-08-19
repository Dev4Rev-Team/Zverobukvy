package ru.gb.zverobukvy.data.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import ru.gb.zverobukvy.domain.entity.Player

@Database(
    entities = [Player::class],
    version = 1,
    exportSchema = false
)
abstract class PlayersDatabase: RoomDatabase() {
    abstract fun playersDao(): PlayersDao

    companion object {
        private var instance: PlayersDatabase? = null

        private const val NAME_PLAYERS_DATABASE = "animal_letters_db"

        private const val DATABASE_HAS_NOT_CREATED = "Database has not created"

        fun getPlayersDatabase(): PlayersDatabase =
            instance ?: throw RuntimeException(DATABASE_HAS_NOT_CREATED)

        fun createInstanceDatabase(context: Context) {
            if (instance == null) {
                instance = Room.databaseBuilder(
                    context,
                    PlayersDatabase::class.java,
                    NAME_PLAYERS_DATABASE
                )
                    .createFromAsset("database/animal_letters_db.db")
                    .build()
            }
        }
    }
}