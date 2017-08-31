package com.example.administrator.hh.download.ex

import java.text.DecimalFormat

/**
 * Created by Administrator on 2017/8/31 0031.
 */
inline fun Int.formatSize(): String = with(this){
    val hrSize: String
    val b = this.toDouble()
    val k = this / 1024.0
    val m = this / 1024.0 / 1024.0
    val g = this / 1024.0 / 1024.0 / 1024.0
    val t = this / 1024.0 / 1024.0 / 1024.0 / 1024.0
    val dec = DecimalFormat("0.00")
    if (t > 1) {
        hrSize = dec.format(t) + " TB"
    } else if (g > 1) {
        hrSize = dec.format(g) + " GB"
    } else if (m > 1) {
        hrSize = dec.format(m) + " MB"
    } else if (k > 1) {
        hrSize = dec.format(k) + " KB"
    } else {
        hrSize = dec.format(b) + " B"
    }
    return hrSize
}

inline fun Long.formatSize(): String =with(this){
    val hrSize: String
    val b = this.toDouble()
    val k = this / 1024.0
    val m = this / 1024.0 / 1024.0
    val g = this / 1024.0 / 1024.0 / 1024.0
    val t = this / 1024.0 / 1024.0 / 1024.0 / 1024.0
    val dec = DecimalFormat("0.00")
    if (t > 1) {
        hrSize = dec.format(t) + " TB"
    } else if (g > 1) {
        hrSize = dec.format(g) + " GB"
    } else if (m > 1) {
        hrSize = dec.format(m) + " MB"
    } else if (k > 1) {
        hrSize = dec.format(k) + " KB"
    } else {
        hrSize = dec.format(b) + " B"
    }
    return hrSize
}