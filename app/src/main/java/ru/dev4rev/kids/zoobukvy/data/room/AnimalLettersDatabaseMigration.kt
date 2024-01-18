package ru.dev4rev.kids.zoobukvy.data.room

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

class AnimalLettersDatabaseMigration {
    companion object{
       val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("DROP TABLE letters")
                database.execSQL("DROP TABLE words")
                database.execSQL("DROP TABLE cards_set")
            }
        }
    }
}