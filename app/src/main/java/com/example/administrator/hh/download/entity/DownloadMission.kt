package com.example.administrator.hh.download.entity

import io.reactivex.processors.FlowableProcessor

/**
 * Created by Administrator on 2017/8/31 0031.
 */
abstract class DownloadMission {
    //    protected var rxdownload: RxDownload
    internal var processor: FlowableProcessor<DownloadEvent>? = null
    private var canceled = false
    private var completed = false

//    internal fun DownloadMission(rxdownload: RxDownload): ??? {
//        this.rxdownload = rxdownload
//    }

    fun isCanceled(): Boolean {
        return canceled
    }

    fun setCanceled(canceled: Boolean) {
        this.canceled = canceled
    }

    fun isCompleted(): Boolean {
        return completed
    }

    fun setCompleted(completed: Boolean) {
        this.completed = completed
    }

    abstract fun getUrl(): String

    abstract fun init(missionMap: Map<String, DownloadMission>,
                      processorMap: Map<String, FlowableProcessor<DownloadEvent>>)

//    abstract fun insertOrUpdate(dataBaseHelper: DataBaseHelper)
//
//    @Throws(InterruptedException::class)
//    abstract fun start(semaphore: Semaphore)
//
//    abstract fun pause(dataBaseHelper: DataBaseHelper)
//
//    abstract fun delete(dataBaseHelper: DataBaseHelper, deleteFile: Boolean)
//
//    abstract fun sendWaitingEvent(dataBaseHelper: DataBaseHelper)
}