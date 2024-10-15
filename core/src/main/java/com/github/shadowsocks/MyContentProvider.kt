package com.github.shadowsocks

import android.content.ContentProvider
import android.content.ContentUris
import android.content.ContentValues
import android.content.UriMatcher
import android.database.Cursor
import android.database.SQLException
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.net.Uri
import android.content.Context

class MyContentProvider : ContentProvider() {

    companion object {
        var AUTHORITY = "com.forest.stable.game.video.fast.easy.mycontentprovider"
        val CONTENT_URI: Uri = Uri.parse("content://$AUTHORITY/data")

        const val DATA_TABLE = "data"
        const val DATA_ID = 1

        val uriMatcher = UriMatcher(UriMatcher.NO_MATCH).apply {
            addURI(AUTHORITY, "data", DATA_ID)
        }
    }

    private lateinit var dbHelper: DatabaseHelper

    override fun onCreate(): Boolean {
        dbHelper = DatabaseHelper(context!!)
        return true
    }

    override fun query(
        uri: Uri,
        projection: Array<String>?,
        selection: String?,
        selectionArgs: Array<String>?,
        sortOrder: String?
    ): Cursor? {
        val db = dbHelper.readableDatabase
        val cursor = when (uriMatcher.match(uri)) {
            DATA_ID -> db.query(
                DATA_TABLE,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                sortOrder
            )

            else -> throw IllegalArgumentException("未知的URI: $uri")
        }
        cursor.setNotificationUri(context?.contentResolver, uri)
        return cursor
    }

    override fun insert(uri: Uri, values: ContentValues?): Uri? {
        val db = dbHelper.writableDatabase
        val id = when (uriMatcher.match(uri)) {
            DATA_ID -> db.insert(DATA_TABLE, null, values)
            else -> throw IllegalArgumentException("未知的URI: $uri")
        }
        if (id > 0) {
            val returnUri = ContentUris.withAppendedId(CONTENT_URI, id)
            context?.contentResolver?.notifyChange(returnUri, null)
            return returnUri
        }
        throw SQLException("插入数据失败: $uri")
    }

    override fun update(
        uri: Uri,
        values: ContentValues?,
        selection: String?,
        selectionArgs: Array<String>?
    ): Int {
        val db = dbHelper.writableDatabase
        val count = when (uriMatcher.match(uri)) {
            DATA_ID -> db.update(DATA_TABLE, values, selection, selectionArgs)
            else -> throw IllegalArgumentException("未知的URI: $uri")
        }
        context?.contentResolver?.notifyChange(uri, null)
        return count
    }

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<String>?): Int {
        val db = dbHelper.writableDatabase
        val count = when (uriMatcher.match(uri)) {
            DATA_ID -> db.delete(DATA_TABLE, selection, selectionArgs)
            else -> throw IllegalArgumentException("未知的URI: $uri")
        }
        context?.contentResolver?.notifyChange(uri, null)
        return count
    }

    override fun getType(uri: Uri): String? {
        return when (uriMatcher.match(uri)) {
            DATA_ID -> "vnd.android.cursor.dir/vnd.$AUTHORITY.$DATA_TABLE"
            else -> throw IllegalArgumentException("未知的URI: $uri")
        }
    }

    private class DatabaseHelper(context: Context) :
        SQLiteOpenHelper(context, "mydatabase.db", null, 1) {

        override fun onCreate(db: SQLiteDatabase) {
            db.execSQL(
                """
                CREATE TABLE $DATA_TABLE (
                    _id INTEGER PRIMARY KEY AUTOINCREMENT,
                    key_name TEXT NOT NULL,
                    value TEXT NOT NULL
                );
                """.trimIndent()
            )
        }

        override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
            db.execSQL("DROP TABLE IF EXISTS $DATA_TABLE")
            onCreate(db)
        }
    }
}
