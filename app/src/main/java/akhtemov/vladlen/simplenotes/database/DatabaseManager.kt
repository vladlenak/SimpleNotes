package akhtemov.vladlen.simplenotes.database

import akhtemov.vladlen.simplenotes.Note
import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase

class DatabaseManager(context: Context) {
    private val databaseHelper = DatabaseHelper(context)
    var sqLiteDatabase: SQLiteDatabase? = null

    fun openDb() {
        sqLiteDatabase = databaseHelper.writableDatabase
    }

    fun insertToDb(title: String, description: String, date: String) {
        val values = ContentValues().apply {
            put(DatabaseConstant.TITLE_COLUMN, title)
            put(DatabaseConstant.DESCRIPTION_COLUMN, description)
            put(DatabaseConstant.DATE_COLUMN, date)
        }

        sqLiteDatabase?.insert(DatabaseConstant.NOTES_TABLE, null, values)
    }

    fun readDbData(): ArrayList<Note> {
        val notesArrayList = ArrayList<Note>()
        val cursor = sqLiteDatabase?.query(
            DatabaseConstant.NOTES_TABLE, null, null,
            null, null, null, null
        )

        while (cursor?.moveToNext()!!) {
            val dataTitle = cursor.getString(cursor.getColumnIndex(DatabaseConstant.TITLE_COLUMN))
            val dataDescription =
                cursor.getString(cursor.getColumnIndex(DatabaseConstant.DESCRIPTION_COLUMN))
            val dataDate =
                cursor.getString(cursor.getColumnIndex(DatabaseConstant.DATE_COLUMN))

            val note = Note
            note.title = dataTitle
            note.description = dataDescription
            note.date = dataDate

            notesArrayList.add(note)
        }
        cursor.close()

        return notesArrayList
    }

    fun closeDb() {
        databaseHelper.close()
    }
}