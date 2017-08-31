package com.example.administrator.hh.download.ex

import com.example.administrator.hh.download.entity.DownloadFlag

/**
 * Created by Administrator on 2017/8/31 0031.
 */
inline fun Int.getDownloadFlag(): DownloadFlag {
    var filter = DownloadFlag.values().filter {
        it.flagInt == this
    }
    if (filter.isEmpty()) {
        return DownloadFlag.NORMAL
    } else {
        return filter[0]
    }
}

inline fun <reified T> T.singletonLock(lock: Any, block: () -> Unit): T {
    this orElse {
        synchronized(lock) {
            block()
        }
    }
    return this!!
}


inline fun <T> orElse(block: () -> T): T = block()

inline infix fun <T : Any?> T.orElse(orBlock: () -> Unit) = if (this == null) orBlock() else this