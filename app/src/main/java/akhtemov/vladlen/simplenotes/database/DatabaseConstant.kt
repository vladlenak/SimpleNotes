package akhtemov.vladlen.simplenotes.database

import android.provider.BaseColumns

object DatabaseConstant {
    const val NOTES_TABLE = "notes_table"

    const val COLUMN_TITLE = "title_column"
    const val COLUMN_DESCRIPTION = "description_column"
    const val COLUMN_DATE = "date_column"

    const val DATABASE_VERSION = 1
    const val DATABASE_NAME = "NotesDatabase.db"

    const val CREATE_TABLE = "CREATE TABLE IF NOT EXISTS $NOTES_TABLE (${BaseColumns._ID} INTEGER PRIMARY KEY, $COLUMN_TITLE TEXT, $COLUMN_DESCRIPTION TEXT, $COLUMN_DATE TEXT)"
    const val DELETE_TABLE = "DROP TABLE IF EXISTS $NOTES_TABLE"
}