package com.example.administrator.hh.ex

import android.content.Context
import android.widget.Toast

/**
 * Created by Administrator on 2017/8/31 0031.
 */
inline fun Context.showToast(msg: String?): Unit {
    Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
}
inline fun <T> orElse(block: () -> T): T = block()

inline infix fun <T : Any?> T.orElse(orBlock: () -> Unit) = if (this == null) orBlock() else this

//三目运算
fun <T> select(isTrue: Boolean, param1: () -> T, param2: () -> T) = if (isTrue) param1() else param2()

fun <T> select(isTrue: Boolean, param1: T, param2: T) = if (isTrue) param1 else param2