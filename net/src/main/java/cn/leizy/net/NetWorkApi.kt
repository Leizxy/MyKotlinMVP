package cn.leizy.net

import okhttp3.OkHttpClient
import retrofit2.Retrofit

/**
 * @author Created by wulei
 * @date 2020/9/17
 * @description
 */
public abstract class NetWorkApi {
    private var okHttpClient: OkHttpClient? = null

    protected fun get(service: Class<*>): Retrofit? {
        val builder: Retrofit.Builder = Retrofit.Builder()
        builder.client(getOkHttpClient()!!).baseUrl("")
        val retrofit: Retrofit = builder.build()
        return retrofit
    }

    private fun getOkHttpClient(): OkHttpClient? {
        if (okHttpClient != null) {
            val okHttpClientBuilder: OkHttpClient.Builder = OkHttpClient.Builder()
            okHttpClient = okHttpClientBuilder.build()
        }
        return okHttpClient
    }
}