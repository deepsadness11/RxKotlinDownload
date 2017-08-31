package com.example.administrator.hh.download.db

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.administrator.hh.download.ex.transaction


internal class DbOpenHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    override fun onCreate(db: SQLiteDatabase) {
        db.transaction {
            execSQL(Db.RecordTable.CREATE)
        }
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        if (oldVersion == 1 && newVersion == 2) {
            db.transaction {
                execSQL(Db.RecordTable.ALTER_TABLE_ADD_EXTRA1)
                execSQL(Db.RecordTable.ALTER_TABLE_ADD_EXTRA2)
                execSQL(Db.RecordTable.ALTER_TABLE_ADD_EXTRA3)
                execSQL(Db.RecordTable.ALTER_TABLE_ADD_EXTRA4)
                execSQL(Db.RecordTable.ALTER_TABLE_ADD_EXTRA5)
                execSQL(Db.RecordTable.ALTER_TABLE_ADD_MISSION_ID)
            }
        }
    }

    companion object {
        private val DATABASE_NAME = "rxdownload_download.db"
        private val DATABASE_VERSION = 2
    }
}
