package cn.leizy.lib.retrofit

import cn.leizy.lib.http.IHttp
import cn.leizy.net.base.NetWorkApi

/**
 * @author Created by wulei
 * @date 2020/9/18, 018
 * @description
 */
class CommonApi : NetWorkApi() {
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

}