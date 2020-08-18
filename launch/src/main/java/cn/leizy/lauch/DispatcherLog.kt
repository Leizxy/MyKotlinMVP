package com.scwlyd.tmslib.lauch

import android.util.Log
import com.scwlyd.tmslib.BuildConfig

/**
 * @author Created by wulei
 * @date 2020/8/7, 007
 * @description
 */
object DispatcherLog {
    val isDebug = BuildConfig.DEBUG

    fun i(msg: String) {
        if (isDebug) {
            Log.i("DispatcherLog", msg)
        }
    }

}