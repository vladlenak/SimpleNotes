package com.octopus.inc.data.room

import androidx.room.*
import com.octopus.inc.data.model.Note

@Dao
interface NoteDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(note: Note)

    @Delete
    suspend fun deleteNote(note: Note)

    @Update
    suspend fun updateNote(note: Note)

    @Query("SELECT * FROM note_table ORDER BY date")
    suspend fun getNotes(): List<Note>
}