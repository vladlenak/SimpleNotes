package com.octopus.inc.data.room

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.octopus.inc.data.room.model.NoteEntity

@Database(entities = [NoteEntity::class], version = 2, exportSchema = true)
abstract class NoteRoomDatabase : RoomDatabase() {

    companion object {
        const val NOTE_ROOM_DATABASE_NAME = "note_database"

        val migration_1_2 = object : Migration(1, 2) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL("ALTER TABLE note_table ADD COLUMN time TEXT NOT NULL DEFAULT ''")
            }
        }
    }

    abstract fun noteDao(): NoteDao
}