package cn.leizy.net.interceptors

import android.util.Log
import okhttp3.Interceptor
import okhttp3.Response

/**
 * @author Created by wulei
 * @date 2020/9/28
 * @description
 */
class ResponseInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val start = System.currentTimeMillis()
        val response = chain.proceed(chain.request())
        Log.i("Total", "requestTime: ${System.currentTimeMillis() - start}")
        return response
    }
}