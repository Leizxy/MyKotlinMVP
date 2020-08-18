package com.scwlyd.tmslib.lauch

import android.util.Log

/**
 * @author Created by wulei
 * @date 2020/8/18, 018
 * @description
 */
object Timing {
    private var startTime: Long = 0
    fun startRecord() {
        startTime = System.currentTimeMillis()
    }

    fun endRecord() {
        endRecord("")
    }

    fun endRecord(msg: String) {
        Log.i("Timing", "{$msg} cost {${System.currentTimeMillis() - startTime}}")
    }
}