package cn.leizy.lib.retrofit

import cn.leizy.lib.http.IHttp
import cn.leizy.net.base.NetWorkApi
import okhttp3.Interceptor
import retrofit2.CallAdapter
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory

/**
 * @author Created by wulei
 * @date 2020/9/18, 018
 * @description
 */
class CommonApi private constructor() : NetWorkApi() {
    companion object {
        @Volatile
        private var instance: CommonApi? = null

        fun getInstance(): CommonApi {
            if (instance == null) {
                synchronized(CommonApi::class.java) {
                    if (instance == null) {
                        instance = CommonApi()
                    }
                }
            }
            return instance!!
        }

        fun <T> getService(service: Class<T>): T {
            return getInstance().getRetrofit(service).create(service)
        }
    }

    override fun getHost(): String {
        return IHttp.HOST
    }

    override fun getCallAdapter(): CallAdapter.Factory {
        return RxJava2CallAdapterFactory.create()
    }

    override fun getInterceptor(): Interceptor? {
        return null
    }

}