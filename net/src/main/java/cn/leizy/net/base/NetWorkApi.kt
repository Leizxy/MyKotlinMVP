package cn.leizy.net.base

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.lang.RuntimeException

/**
 * @author Created by wulei
 * @date 2020/9/17
 * @description
 */
abstract class NetWorkApi constructor() {
    private var baseUrl: String? = null

    init {
        baseUrl = this.getHost()
    }

    companion object {
        private val retrofits: HashMap<String, Retrofit> = HashMap()
        fun init() {
            // TODO: 2020/9/18, 018 初始化
        }
    }

    private var okHttpClient: OkHttpClient? = null

    abstract fun getHost(): String

    protected fun getRetrofit(service: Class<*>): Retrofit {
        if (baseUrl == null) {
            throw RuntimeException("your getHost() method had returned null.")
        }
        if (retrofits[baseUrl + service.simpleName] != null) {
            return retrofits[baseUrl + service.simpleName]!!
        }
        val builder: Retrofit.Builder = Retrofit.Builder()
        builder.client(getOkHttpClient()!!)
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        val retrofit: Retrofit = builder.build()
        retrofit.create(service)
        retrofits[baseUrl + service.simpleName] = retrofit
        return retrofit
    }

    private fun getOkHttpClient(): OkHttpClient? {
        if (okHttpClient == null) {
            val okHttpClientBuilder: OkHttpClient.Builder = OkHttpClient.Builder()
            okHttpClient = okHttpClientBuilder.build()
        }
        return okHttpClient
    }
}