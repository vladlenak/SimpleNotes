package com.octopus.inc.data.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.octopus.inc.data.model.Note

@Database(entities = [Note::class], version = 1, exportSchema = true)
abstract class NoteRoomDatabase : RoomDatabase() {
    abstract fun noteDao(): NoteDao
}