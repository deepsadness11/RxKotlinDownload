package com.example.administrator.hh.download.ex

import android.text.TextUtils

/**
 * Created by Administrator on 2017/8/31 0031.
 */
inline fun String.isEmpty(): Boolean = TextUtils.isEmpty(this)

inline infix fun String.doEmpty(block: () -> Unit): Unit {
    if (isEmpty()) {
        block()
    }
}

//inline infix fun <T : Any?> T.orElse(orBlock: () -> Unit) = if (this == null) orBlock() else this