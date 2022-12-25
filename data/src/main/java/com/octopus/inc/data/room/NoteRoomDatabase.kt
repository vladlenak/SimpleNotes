package com.octopus.inc.data.room

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.octopus.inc.data.model.Note

@Database(entities = [Note::class], version = 2, exportSchema = true)
abstract class NoteRoomDatabase : RoomDatabase() {

    companion object {
        val migration_1_2 = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("ALTER TABLE note_table ADD COLUMN time TEXT NOT NULL DEFAULT ''")
            }
        }
    }

    abstract fun noteDao(): NoteDao
}