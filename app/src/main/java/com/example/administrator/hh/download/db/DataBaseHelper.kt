package com.example.administrator.hh.download.db

import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import com.example.administrator.hh.download.entity.DownloadBean
import com.example.administrator.hh.download.entity.DownloadFlag
import com.example.administrator.hh.download.entity.DownloadRecord
import com.example.administrator.hh.download.entity.DownloadStatus
import com.example.administrator.hh.download.ex.orElse
import com.example.administrator.hh.download.ex.singletonLock
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.util.*

/**
 * 静态单例
 * Created by Administrator on 2017/8/31 0031.
 */
class DataBaseHelper private constructor(context: Context) {
    private val mDbOpenHelper: DbOpenHelper = DbOpenHelper(context)
    private val databaseLock = Any()
    @Volatile private var readableDatabase: SQLiteDatabase? = null
    @Volatile private var writableDatabase: SQLiteDatabase? = null

    //完成单例
    companion object {
        @Volatile private var singleton: DataBaseHelper? = null
        fun getSingleton(context: Context): DataBaseHelper {
            singleton orElse {
                synchronized(DataBaseHelper::class.java) {
                    singleton orElse {
                        singleton = DataBaseHelper(context)
                    }
                }
            }
            return singleton!!
        }
    }

    private fun getWritableDatabase(): SQLiteDatabase {
        var db: SQLiteDatabase? = writableDatabase
        return db.singletonLock(databaseLock) {
            db = writableDatabase
            db orElse {
                writableDatabase = mDbOpenHelper.writableDatabase
                db = writableDatabase
            }
        }!!
    }

    private fun getReadableDatabase(): SQLiteDatabase {
        var db: SQLiteDatabase? = readableDatabase
        return db.singletonLock(databaseLock) {
            db = readableDatabase
            db orElse {
                readableDatabase = mDbOpenHelper.readableDatabase
                db = readableDatabase
            }
        }!!
    }

    fun closeDataBase() {
        synchronized(databaseLock) {
            readableDatabase = null
            writableDatabase = null
            mDbOpenHelper.close()
        }
    }

    //正常的方法
    /**
     * Judge the url's record exists.

     * @param url url
     * *
     * @return true if not exists
     */
    fun recordNotExists(url: String): Boolean {
        var cursor: Cursor? = null
        var result = false
        cursor = getReadableDatabase().query(Db.RecordTable.TABLE_NAME, arrayOf<String>(Db.RecordTable.COLUMN_ID),
                Db.RecordTable.COLUMN_URL + "=?", arrayOf(url), null, null, null)
        cursor.use {
            cursor!!.moveToFirst()
            result = cursor!!.count == 0
        }
        return result
    }

    fun insertRecord(downloadBean: DownloadBean, flag: DownloadFlag): Long {
        return getWritableDatabase().insert(Db.RecordTable.TABLE_NAME, null, Db.RecordTable.insert(downloadBean, flag, null))
    }

    fun insertRecord(downloadBean: DownloadBean, flag: DownloadFlag, missionId: String): Long {
        return getWritableDatabase().insert(Db.RecordTable.TABLE_NAME, null, Db.RecordTable.insert(downloadBean, flag, missionId))
    }

    fun updateStatus(url: String, status: DownloadStatus): Long {
        return getWritableDatabase().update(Db.RecordTable.TABLE_NAME, Db.RecordTable.update(status),
                Db.RecordTable.COLUMN_URL + "=?", arrayOf(url)).toLong()
    }

    fun updateRecord(url: String, flag: DownloadFlag): Long {
        return getWritableDatabase().update(Db.RecordTable.TABLE_NAME, Db.RecordTable.update(flag),
                Db.RecordTable.COLUMN_URL + "=?", arrayOf(url)).toLong()
    }

    fun updateRecord(url: String, flag: DownloadFlag, missionId: String): Long {
        return getWritableDatabase().update(Db.RecordTable.TABLE_NAME, Db.RecordTable.update(flag, missionId),
                Db.RecordTable.COLUMN_URL + "=?", arrayOf(url)).toLong()
    }

    fun updateRecord(url: String, saveName: String, savePath: String, flag: DownloadFlag): Long {
        return getWritableDatabase().update(Db.RecordTable.TABLE_NAME, Db.RecordTable.update(saveName, savePath, flag),
                Db.RecordTable.COLUMN_URL + "=?", arrayOf(url)).toLong()
    }

    fun deleteRecord(url: String): Int {
        return getWritableDatabase().delete(Db.RecordTable.TABLE_NAME, Db.RecordTable.COLUMN_URL + "=?", arrayOf(url))
    }

    fun repairErrorFlag(): Long {
        return getWritableDatabase().update(Db.RecordTable.TABLE_NAME, Db.RecordTable.update(DownloadFlag.PAUSED),
                Db.RecordTable.COLUMN_DOWNLOAD_FLAG + "=? or " + Db.RecordTable.COLUMN_DOWNLOAD_FLAG + "=?",
                arrayOf<String>(DownloadFlag.WAITING.flagInt.toString(), DownloadFlag.STARTED.flagInt.toString())).toLong()
    }

    /**
     * Read single Record.

     * @param url url
     * *
     * @return Record
     */
    fun readSingleRecord(url: String): DownloadRecord? {
        var record: DownloadRecord? = null
        val cursor = getReadableDatabase().query(Db.RecordTable.TABLE_NAME,
                arrayOf<String>(
                        Db.RecordTable.COLUMN_ID,
                        Db.RecordTable.COLUMN_URL,
                        Db.RecordTable.COLUMN_SAVE_NAME,
                        Db.RecordTable.COLUMN_SAVE_PATH,
                        Db.RecordTable.COLUMN_DOWNLOAD_SIZE,
                        Db.RecordTable.COLUMN_TOTAL_SIZE,
                        Db.RecordTable.COLUMN_IS_CHUNKED,
                        Db.RecordTable.COLUMN_EXTRA1,
                        Db.RecordTable.COLUMN_EXTRA2,
                        Db.RecordTable.COLUMN_EXTRA3,
                        Db.RecordTable.COLUMN_EXTRA4,
                        Db.RecordTable.COLUMN_EXTRA5,
                        Db.RecordTable.COLUMN_DOWNLOAD_FLAG,
                        Db.RecordTable.COLUMN_DATE,
                        Db.RecordTable.COLUMN_MISSION_ID),
                Db.RecordTable.COLUMN_URL + "=?",
                arrayOf(url), null, null, null)
        cursor.use {
            cursor!!.moveToFirst()
            if (cursor.count != 0) {
                record = Db.RecordTable.read(cursor)
            }
        }
        return record
    }

    /**
     * Read missionId all records.

     * @param missionId missionId
     * *
     * @return Records
     */
    fun readMissionsRecord(missionId: String): List<DownloadRecord> {
        var cursor: Cursor? = null
        cursor = getReadableDatabase().query(Db.RecordTable.TABLE_NAME,
                arrayOf<String>(
                        Db.RecordTable.COLUMN_ID,
                        Db.RecordTable.COLUMN_URL,
                        Db.RecordTable.COLUMN_SAVE_NAME,
                        Db.RecordTable.COLUMN_SAVE_PATH,
                        Db.RecordTable.COLUMN_DOWNLOAD_SIZE,
                        Db.RecordTable.COLUMN_TOTAL_SIZE,
                        Db.RecordTable.COLUMN_IS_CHUNKED,
                        Db.RecordTable.COLUMN_EXTRA1,
                        Db.RecordTable.COLUMN_EXTRA2,
                        Db.RecordTable.COLUMN_EXTRA3,
                        Db.RecordTable.COLUMN_EXTRA4,
                        Db.RecordTable.COLUMN_EXTRA5,
                        Db.RecordTable.COLUMN_DOWNLOAD_FLAG,
                        Db.RecordTable.COLUMN_DATE,
                        Db.RecordTable.COLUMN_MISSION_ID),
                Db.RecordTable.COLUMN_MISSION_ID + "=?", arrayOf(missionId), null, null, null)
        val result = ArrayList<DownloadRecord>()
        cursor.use {
            cursor!!.moveToFirst()
            if (cursor!!.count > 0) {
                do {
                    result.add(Db.RecordTable.read(cursor!!))
                } while (cursor!!.moveToNext())
            }
        }
        return result
    }

    /**
     * Read the url's download status.

     * @param url url
     * *
     * @return download status
     */
    fun readStatus(url: String): DownloadStatus {
        var cursor: Cursor? = null
        var ds: DownloadStatus? = null
        cursor = getReadableDatabase().query(
                Db.RecordTable.TABLE_NAME,
                arrayOf<String>(
                        Db.RecordTable.COLUMN_DOWNLOAD_SIZE,
                        Db.RecordTable.COLUMN_TOTAL_SIZE,
                        Db.RecordTable.COLUMN_IS_CHUNKED),
                Db.RecordTable.COLUMN_URL + "=?", arrayOf(url), null, null, null)
        cursor.use {
            cursor!!.moveToFirst()
            if (cursor!!.count == 0) {
                ds = DownloadStatus()
            } else {
                ds = Db.RecordTable.readStatus(cursor!!)
            }
        }
        return ds!!
    }

    fun readAllRecords(): Observable<List<DownloadRecord>> = Observable.create<List<DownloadRecord>> {
        e ->
        val column = arrayOf<String>(
                Db.RecordTable.COLUMN_ID,
                Db.RecordTable.COLUMN_URL,
                Db.RecordTable.COLUMN_SAVE_NAME,
                Db.RecordTable.COLUMN_SAVE_PATH,
                Db.RecordTable.COLUMN_DOWNLOAD_SIZE,
                Db.RecordTable.COLUMN_TOTAL_SIZE,
                Db.RecordTable.COLUMN_IS_CHUNKED,
                Db.RecordTable.COLUMN_EXTRA1,
                Db.RecordTable.COLUMN_EXTRA2,
                Db.RecordTable.COLUMN_EXTRA3,
                Db.RecordTable.COLUMN_EXTRA4,
                Db.RecordTable.COLUMN_EXTRA5,
                Db.RecordTable.COLUMN_DOWNLOAD_FLAG,
                Db.RecordTable.COLUMN_DATE,
                Db.RecordTable.COLUMN_MISSION_ID
        )
        val cursor = getReadableDatabase().query(Db.RecordTable.TABLE_NAME, column,
                null, null, null, null, null
        )
        val result = arrayListOf<DownloadRecord>()
        cursor.use {
            if (cursor.count > 0) {
                do {
                    result.add(Db.RecordTable.read(cursor))
                } while (cursor.moveToNext())
            }
            e!!.onNext(result)
            e.onComplete()
        }
    }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())

    fun readRecord(url: String) = Observable.create<DownloadRecord> {
        e ->
        val column = arrayOf<String>(
                Db.RecordTable.COLUMN_ID,
                Db.RecordTable.COLUMN_URL,
                Db.RecordTable.COLUMN_SAVE_NAME,
                Db.RecordTable.COLUMN_SAVE_PATH,
                Db.RecordTable.COLUMN_DOWNLOAD_SIZE,
                Db.RecordTable.COLUMN_TOTAL_SIZE,
                Db.RecordTable.COLUMN_IS_CHUNKED,
                Db.RecordTable.COLUMN_EXTRA1,
                Db.RecordTable.COLUMN_EXTRA2,
                Db.RecordTable.COLUMN_EXTRA3,
                Db.RecordTable.COLUMN_EXTRA4,
                Db.RecordTable.COLUMN_EXTRA5,
                Db.RecordTable.COLUMN_DOWNLOAD_FLAG,
                Db.RecordTable.COLUMN_DATE,
                Db.RecordTable.COLUMN_MISSION_ID
        )

        val cursor = getReadableDatabase().query(Db.RecordTable.TABLE_NAME,
                column,
                Db.RecordTable.COLUMN_URL + "=?",
                arrayOf<String>(url),
                null, null, null)

        cursor.use {
            cursor.moveToFirst()
            if (cursor.count == 0) {
                e.onNext(DownloadRecord())
            } else {
                e.onNext(Db.RecordTable.read(cursor))
            }
            e.onComplete()
        }
    }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())


}