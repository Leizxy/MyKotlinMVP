package cn.leizy.lib.http

import android.app.Application
import cn.leizy.lib.BuildConfig
import com.lzy.okgo.OkGo

/**
 * @author Created by wulei
 * @date 2019-11-11
 * @description
 */
interface IHttp {
    companion object {
        const val DEFAULT_MILLISECONDS: Long = OkGo.DEFAULT_MILLISECONDS
        const val HOST: String = BuildConfig.APP_HOST

        const val LOGIN: String = "driver/login"
    }

    fun initHttp(context: Application)

    fun <T> get(url: String, params: Map<String, Any>?, callback: BaseCallback<T>)

    fun <T> post(url: String, tag: Any, params: Map<String, Any>?, callback: BaseCallback<T>)

    fun cancel(tag: Any)

    fun addHeader(key: String, value: String)
}