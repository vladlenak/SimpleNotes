package akhtemov.vladlen.simplenotes.database

import android.provider.BaseColumns

object DatabaseConstant {
    const val NOTES_TABLE = "notes_table"
    const val TITLE_COLUMN = "title_column"
    const val DESCRIPTION_COLUMN = "description_column"
    const val DATE_COLUMN = "date_column"

    const val DATABASE_VERSION = 1
    const val DATABASE_NAME = "NotesDatabase.db"

    const val CREATE_TABLE = "CREATE TABLE IF NOT EXISTS $NOTES_TABLE (${BaseColumns._ID} INTEGER PRIMARY KEY, $TITLE_COLUMN TEXT, $DESCRIPTION_COLUMN TEXT, $DATE_COLUMN TEXT)"
    const val DELETE_TABLE = "DROP TABLE IF EXISTS $NOTES_TABLE"
}