package com.octopus.inc.data.room

import androidx.room.*
import com.octopus.inc.data.model.NoteEntity

@Dao
interface NoteDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(note: NoteEntity)

    @Delete
    suspend fun deleteNote(note: NoteEntity)

    @Update
    suspend fun updateNote(note: NoteEntity)

    @Query("SELECT * FROM note_table ORDER BY date")
    suspend fun getNotes(): List<NoteEntity>

    @Query("SELECT * FROM note_table WHERE id=:noteId ")
    suspend fun getNote(noteId: String): NoteEntity
}