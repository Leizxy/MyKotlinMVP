package cn.leizy.net.interceptors

import cn.leizy.net.base.IRequiredInfo
import okhttp3.Interceptor
import okhttp3.Response

/**
 * @author Created by wulei
 * @date 2020/9/28
 * @description
 */
class RequestInterceptor constructor(val iRequiredInfo: IRequiredInfo) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val builder = chain.request().newBuilder()
        builder.addHeader("key", "value")
        return chain.proceed(builder.build())
    }
}