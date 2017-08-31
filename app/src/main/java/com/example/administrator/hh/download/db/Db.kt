package com.example.administrator.hh.download.db

import android.content.ContentValues
import android.database.Cursor
import com.example.administrator.hh.download.entity.DownloadBean
import com.example.administrator.hh.download.entity.DownloadFlag
import com.example.administrator.hh.download.entity.DownloadRecord
import com.example.administrator.hh.download.entity.DownloadStatus
import com.example.administrator.hh.download.ex.doEmpty
import com.example.administrator.hh.download.ex.getDownloadFlag
import java.util.*

/**
 * Created by Administrator on 2017/8/31 0031.
 */
internal object Db {
    internal object RecordTable {
        val TABLE_NAME = "download_record"

        val COLUMN_ID = "id"
        val COLUMN_URL = "url"
        val COLUMN_SAVE_NAME = "save_name"
        val COLUMN_SAVE_PATH = "save_path"
        val COLUMN_DOWNLOAD_SIZE = "download_size"
        val COLUMN_TOTAL_SIZE = "total_size"
        val COLUMN_IS_CHUNKED = "is_chunked"
        val COLUMN_DOWNLOAD_FLAG = "download_flag"
        val COLUMN_EXTRA1 = "extra1"
        val COLUMN_EXTRA2 = "extra2"
        val COLUMN_EXTRA3 = "extra3"
        val COLUMN_EXTRA4 = "extra4"
        val COLUMN_EXTRA5 = "extra5"
        val COLUMN_DATE = "date"
        val COLUMN_MISSION_ID = "mission_id"

        val CREATE =
                "CREATE TABLE " + TABLE_NAME + " (" +
                        COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                        COLUMN_URL + " TEXT NOT NULL," +
                        COLUMN_SAVE_NAME + " TEXT," +
                        COLUMN_SAVE_PATH + " TEXT," +
                        COLUMN_TOTAL_SIZE + " INTEGER," +
                        COLUMN_DOWNLOAD_SIZE + " INTEGER," +
                        COLUMN_IS_CHUNKED + " INTEGER," +
                        COLUMN_DOWNLOAD_FLAG + " INTEGER," +
                        COLUMN_EXTRA1 + " TEXT," +
                        COLUMN_EXTRA2 + " TEXT," +
                        COLUMN_EXTRA3 + " TEXT," +
                        COLUMN_EXTRA4 + " TEXT," +
                        COLUMN_EXTRA5 + " TEXT," +
                        COLUMN_DATE + " INTEGER NOT NULL, " +
                        COLUMN_MISSION_ID + " TEXT " +
                        " )"

        val ALTER_TABLE_ADD_EXTRA1 = "ALTER TABLE $TABLE_NAME ADD $COLUMN_EXTRA1 TEXT"
        val ALTER_TABLE_ADD_EXTRA2 = "ALTER TABLE $TABLE_NAME ADD $COLUMN_EXTRA2 TEXT"
        val ALTER_TABLE_ADD_EXTRA3 = "ALTER TABLE $TABLE_NAME ADD $COLUMN_EXTRA3 TEXT"
        val ALTER_TABLE_ADD_EXTRA4 = "ALTER TABLE $TABLE_NAME ADD $COLUMN_EXTRA4 TEXT"
        val ALTER_TABLE_ADD_EXTRA5 = "ALTER TABLE $TABLE_NAME ADD $COLUMN_EXTRA5 TEXT"
        val ALTER_TABLE_ADD_MISSION_ID = "ALTER TABLE $TABLE_NAME ADD $COLUMN_MISSION_ID TEXT"

        fun insert(bean: DownloadBean, flag: DownloadFlag, missionId: String?): ContentValues {
            val values = ContentValues()
            values.put(COLUMN_URL, bean.url)
            values.put(COLUMN_SAVE_NAME, bean.saveName)
            values.put(COLUMN_SAVE_PATH, bean.savePath)
            values.put(COLUMN_DOWNLOAD_FLAG, flag.flagInt)
            values.put(COLUMN_EXTRA1, bean.extra1)
            values.put(COLUMN_EXTRA2, bean.extra2)
            values.put(COLUMN_EXTRA3, bean.extra3)
            values.put(COLUMN_EXTRA4, bean.extra4)
            values.put(COLUMN_EXTRA5, bean.extra5)
            values.put(COLUMN_DATE, Date().time)
            missionId.doEmpty {
                values.put(COLUMN_MISSION_ID, missionId)
            }
            return values
        }

        fun update(status: DownloadStatus): ContentValues {
            val values = ContentValues()
            values.put(COLUMN_IS_CHUNKED, status.isChunked)
            values.put(COLUMN_DOWNLOAD_SIZE, status.downloadSize)
            values.put(COLUMN_TOTAL_SIZE, status.totalSize)
            return values
        }

        fun update(flag: DownloadFlag): ContentValues {
            val values = ContentValues()
            values.put(COLUMN_DOWNLOAD_FLAG, flag.flagInt)
            return values
        }

        fun update(flag: DownloadFlag, missionId: String): ContentValues {
            val values = ContentValues()
            values.put(COLUMN_DOWNLOAD_FLAG, flag.flagInt)
            missionId.doEmpty {
                values.put(COLUMN_MISSION_ID, missionId)
            }
            return values
        }

        fun update(saveName: String, savePath: String, flag: DownloadFlag): ContentValues {
            val values = ContentValues()
            values.put(COLUMN_SAVE_NAME, saveName)
            values.put(COLUMN_SAVE_PATH, savePath)
            values.put(COLUMN_DOWNLOAD_FLAG, flag.flagInt)
            return values
        }

        fun readStatus(cursor: Cursor): DownloadStatus {
            val isChunked = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_IS_CHUNKED)) > 0
            val downloadSize = cursor.getLong(cursor.getColumnIndexOrThrow(COLUMN_DOWNLOAD_SIZE))
            val totalSize = cursor.getLong(cursor.getColumnIndexOrThrow(COLUMN_TOTAL_SIZE))
            return DownloadStatus(isChunked, downloadSize, totalSize)
        }

        fun read(cursor: Cursor): DownloadRecord {
            val record = DownloadRecord()
            record.id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID))
            record.url = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_URL))
            record.saveName = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_SAVE_NAME))
            record.savePath = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_SAVE_PATH))

            val isChunked = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_IS_CHUNKED)) > 0
            val downloadSize = cursor.getLong(cursor.getColumnIndexOrThrow(COLUMN_DOWNLOAD_SIZE))
            val totalSize = cursor.getLong(cursor.getColumnIndexOrThrow(COLUMN_TOTAL_SIZE))
            record.status = (DownloadStatus(isChunked, downloadSize, totalSize))
            record.extra1 = (cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_EXTRA1)))
            record.extra2 = (cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_EXTRA2)))
            record.extra3 = (cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_EXTRA3)))
            record.extra4 = (cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_EXTRA4)))
            record.extra5 = (cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_EXTRA5)))
            record.flag = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_DOWNLOAD_FLAG)).getDownloadFlag()
            record.date = (cursor.getLong(cursor.getColumnIndexOrThrow(COLUMN_DATE)))
            record.missionId = (cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_MISSION_ID)))
            return record
        }
    }
}