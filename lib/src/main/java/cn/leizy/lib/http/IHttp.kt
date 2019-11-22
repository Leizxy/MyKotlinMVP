package cn.leizy.lib.http

import android.app.Application
import cn.leizy.lib.http.okgo.SimpleCallback

/**
 * @author Created by wulei
 * @date 2019-11-11
 * @description
 */
interface IHttp {
    companion object {
        val LOGIN_HOST: String
            get() = "http://192.168.200.170:9001"
        val HOST: String
            get() = "http://192.168.200.170:9004"

        val LOGIN: String
            get() = "/connect/token"

        val LOGIN_URL: String
            get() = LOGIN_HOST + LOGIN

        val HTTP_TOAST: String
            get() = "http_toast"
    }

    fun initHttp(context: Application)

    fun <T> get(url: String, params: Map<String, Any>?, callback: SimpleCallback<T>)

    fun <T> post(url: String, tag: Any, params: Map<String, Any>?, callback: SimpleCallback<T>)

    fun cancel(tag: Any)

    fun addHeader(key: String, value: String)
}