package cn.leizy.lib.retrofit

import cn.leizy.lib.http.IHttp
import cn.leizy.net.base.NetWorkApi
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import okhttp3.Interceptor
import retrofit2.CallAdapter

/**
 * @author Created by wulei
 * @date 2020/9/22, 022
 * @description
 */
class SuspendApi : NetWorkApi() {
    companion object {
        @Volatile
        private var instance: SuspendApi? = null
        fun getInstance(): SuspendApi {
            if (instance == null) {
                synchronized(SuspendApi::class.java) {
                    if (instance == null) {
                        instance = SuspendApi()
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
        return CoroutineCallAdapterFactory.invoke()
    }

    override fun getInterceptor(): Interceptor? {
        return null
    }
}