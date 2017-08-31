package com.example.administrator.hh.download.ex

import android.database.sqlite.SQLiteDatabase

/**
 * Created by Administrator on 2017/8/31 0031.
 */
inline fun SQLiteDatabase.transaction(transaction: SQLiteDatabase.() -> Unit): Unit {
    beginTransaction()
    try {
        transaction()
        setTransactionSuccessful()
    } finally {
        endTransaction()
    }
}