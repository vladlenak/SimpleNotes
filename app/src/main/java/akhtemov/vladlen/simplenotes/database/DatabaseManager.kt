package akhtemov.vladlen.simplenotes.database

import akhtemov.vladlen.simplenotes.Note
import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase

class DatabaseManager(context: Context) {

    private var databaseHelper = DatabaseHelper(context)
    private var sqLiteDatabase: SQLiteDatabase? = null

    fun openDatabase() {
        sqLiteDatabase = databaseHelper.writableDatabase
    }

    fun insertToDatabase(title: String, description: String, date: String) {
        val values = ContentValues().apply {
            put(DatabaseConstant.TITLE_COLUMN, title)
            put(DatabaseConstant.DESCRIPTION_COLUMN, description)
            put(DatabaseConstant.DATE_COLUMN, date)
        }

        sqLiteDatabase?.insert(DatabaseConstant.NOTES_TABLE, null, values)
    }

    fun readDatabaseData(): ArrayList<Note> {
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

    fun closeDatabase() {
        databaseHelper.close()
    }
}