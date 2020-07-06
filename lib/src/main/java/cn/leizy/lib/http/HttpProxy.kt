package cn.leizy.lib.http

import android.app.Application
import cn.leizy.lib.http.okgo.OkGoHttp
import cn.leizy.lib.http.okgo.NetCallback
import java.lang.StringBuilder

/**
 * @author Created by wulei
 * @date 2019-11-11
 * @description
 */
class HttpProxy private constructor(val iHttp: IHttp) : IHttp {


    override fun initHttp(context: Application) {
        iHttp.initHttp(context)
    }

    override fun <T> get(url: String, params: Map<String, Any>?, callback: NetCallback<T>) {
        iHttp.get(url, params, callback)
    }

    override fun <T> post(url: String, tag: Any, params: Map<String, Any>?, callback: NetCallback<T>) {
        iHttp.post(url, tag, params, callback)
    }

    override fun cancel(tag: Any) {
        iHttp.cancel(tag)
    }

    override fun addHeader(key: String, value: String) {
        iHttp.addHeader(key, value)
    }

    companion object {
        @Volatile
        private var instance: HttpProxy? = null

        fun init(context: Application) {
            if (instance == null) {
                synchronized(HttpProxy::class.java) {
                    if (instance == null) {
                        instance =
                            HttpProxy(OkGoHttp())
                        instance!!.initHttp(context)
                    }
                }
            }
        }

        fun get(): HttpProxy? {
            return instance
        }

        fun buildUrl(vararg strings: String): String {
            val stringBuilder = StringBuilder()
            stringBuilder.append(IHttp.HOST)
            for (str in strings) {
                stringBuilder.append(str)
            }
            return stringBuilder.toString()
        }
    }
}