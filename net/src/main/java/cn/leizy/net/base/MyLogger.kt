package cn.leizy.net.base

import android.util.Log
import okhttp3.logging.HttpLoggingInterceptor

/**
 * @author Created by wulei
 * @date 2020/9/22, 022
 * @description
 */
class MyLogger : HttpLoggingInterceptor.Logger {
    override fun log(message: String) {
        Log.w("leizy", message)
    }
}