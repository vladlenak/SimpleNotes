package akhtemov.vladlen.simplenotes.database

import akhtemov.vladlen.simplenotes.Note
import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.provider.BaseColumns

class DatabaseManager(context: Context) {

    private var databaseHelper = DatabaseHelper(context)
    private var sqLiteDatabase: SQLiteDatabase? = null

    fun openDatabase() {
        sqLiteDatabase = databaseHelper.writableDatabase
    }

    fun insertToDatabase(title: String, description: String, date: String) {
        val values = ContentValues().apply {
            put(DatabaseConstant.COLUMN_TITLE, title)
            put(DatabaseConstant.COLUMN_DESCRIPTION, description)
            put(DatabaseConstant.COLUMN_DATE, date)
        }

        sqLiteDatabase?.insert(DatabaseConstant.NOTES_TABLE, null, values)
    }

    fun removeFromDatabase(id: String) {
        val whereClause = BaseColumns._ID + "=$id"

        sqLiteDatabase?.delete(DatabaseConstant.NOTES_TABLE, whereClause, null)
    }

    fun readDatabaseData(): ArrayList<Note> {
        val notesArrayList = ArrayList<Note>()
        val cursor = sqLiteDatabase?.query(
            DatabaseConstant.NOTES_TABLE, null, null,
            null, null, null, null
        )

        while (cursor?.moveToNext()!!) {
            val dataId = cursor.getInt(cursor.getColumnIndex(BaseColumns._ID))
            val dataTitle = cursor.getString(cursor.getColumnIndex(DatabaseConstant.COLUMN_TITLE))
            val dataDescription =
                cursor.getString(cursor.getColumnIndex(DatabaseConstant.COLUMN_DESCRIPTION))
            val dataDate =
                cursor.getString(cursor.getColumnIndex(DatabaseConstant.COLUMN_DATE))
            val note = Note()

            note.id = dataId
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

    fun updateItem(title: String, description: String, date: String, pos: String) {
        val values = ContentValues().apply {
            put(DatabaseConstant.COLUMN_TITLE, title)
            put(DatabaseConstant.COLUMN_DESCRIPTION, description)
            put(DatabaseConstant.COLUMN_DATE, date)
        }
        val whereClause = BaseColumns._ID + "=$pos"
        sqLiteDatabase?.update(DatabaseConstant.NOTES_TABLE, values, whereClause, null)
    }


}