package com.example.administrator.hh.download.entity

import android.os.Parcel
import android.os.Parcelable
import com.example.administrator.hh.download.ex.formatSize

/**
 * Created by Administrator on 2017/8/31 0031.
 */
/**
 * 下载状态的枚举类
 */
enum class DownloadFlag(val flagInt: Int) {
    NORMAL(9990), //未下载
    WAITING(9991), //等待中
    STARTED(9992), //已开始下载
    PAUSED(9993), //已暂停
    CANCELED(9994), //已取消
    COMPLETED(9995), //已完成
    FAILED(9996), //下载失败
    INSTALL(9997), //安装中,暂未使用
    INSTALLED(9998), //已安装,暂未使用
    DELETED(9999), //已删除
}

/**
 * 下载状态的类
 */
data class DownloadStatus(var isChunked: Boolean = false, var totalSize: Long = 0, var downloadSize: Long = 0)
    : Parcelable {

    constructor(parcel: Parcel) : this() {
        isChunked = parcel.readByte().toInt() != 0
        totalSize = parcel.readLong()
        downloadSize = parcel.readLong()
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeByte(if (this.isChunked) 1.toByte() else 0.toByte())
        dest.writeLong(this.totalSize)
        dest.writeLong(this.downloadSize)
    }

    override fun describeContents(): Int {
        return 0
    }

    fun getFormatTotalSize(): String = totalSize.formatSize()
    fun getFormatDownloadSize(): String = downloadSize.formatSize()

    companion object CREATOR : Parcelable.Creator<DownloadStatus> {
        override fun createFromParcel(parcel: Parcel): DownloadStatus {
            return DownloadStatus(parcel)
        }

        override fun newArray(size: Int): Array<DownloadStatus?> {
            return arrayOfNulls(size)
        }
    }
}

/**
 * 下载的bean
 */
data class DownloadBean(val url: String,
                        var saveName: String? = null,
                        var savePath: String? = null,
                        var extra1: String? = null,
                        var extra2: String? = null,
                        var extra3: String? = null,
                        var extra4: String? = null,
                        var extra5: String? = null)

/**
 * 下载的事件
 */
class DownloadEvent(private var flag: DownloadFlag = DownloadFlag.NORMAL,
                    private var downloadStatus: DownloadStatus = DownloadStatus(),
                    private var mError: Throwable? = null)

/**
 * 下载的进度
 */
class DownloadRange(var start: Long = 0,
                    var end: Long = 0) {
    val size: Long
        get() = end - start + 1

    fun legal(): Boolean {
        return start <= end
    }
}

/**
 * 下载记录的信息
 */
data class DownloadRecord(var id: Int = -1,
                          var url: String? = null,
                          var saveName: String? = null,
                          var savePath: String? = null,
                          var status: DownloadStatus? = null,
                          var flag: DownloadFlag = DownloadFlag.NORMAL,
                          var extra1: String? = null,
                          var extra2: String? = null,
                          var extra3: String? = null,
                          var extra4: String? = null,
                          var extra5: String? = null,
                          var date: Long = 0, //格林威治时间,毫秒
                          var missionId: String? = null
)
