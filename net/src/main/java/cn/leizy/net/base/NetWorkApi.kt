package cn.leizy.net.base

import cn.leizy.net.interceptors.RequestInterceptor
import cn.leizy.net.interceptors.ResponseInterceptor
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.CallAdapter
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.lang.RuntimeException

/**
 * @author Created by wulei
 * @date 2020/9/17
 * @description
 */
abstract class NetWorkApi {
    private var baseUrl: String? = null

    init {
        baseUrl = this.getHost()
    }

    companion object {
        private val retrofits: HashMap<String, Retrofit> = HashMap()
        private var iRequiredInfo: IRequiredInfo? = null
        fun init(iRequiredInfo: IRequiredInfo) {
            this.iRequiredInfo = iRequiredInfo
        }
    }

    private var okHttpClient: OkHttpClient? = null

    abstract fun getHost(): String

    abstract fun getCallAdapter(): CallAdapter.Factory

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
            .addCallAdapterFactory(getCallAdapter())
        val retrofit: Retrofit = builder.build()
        retrofit.create(service)
        retrofits[baseUrl + service.simpleName] = retrofit
        return retrofit
    }

    private fun getOkHttpClient(): OkHttpClient? {
        if (okHttpClient == null) {
            val okHttpClientBuilder: OkHttpClient.Builder = OkHttpClient.Builder()
            getInterceptor()?.let { okHttpClientBuilder.addInterceptor(getInterceptor()!!) }
            okHttpClientBuilder.addInterceptor(RequestInterceptor(iRequiredInfo!!))
            okHttpClientBuilder.addInterceptor(ResponseInterceptor())
            if (iRequiredInfo!!.isDebug()) {
                val log = HttpLoggingInterceptor(MyLogger())
                log.setLevel(HttpLoggingInterceptor.Level.BODY)
                okHttpClientBuilder.addInterceptor(log)
            }
            okHttpClient = okHttpClientBuilder.build()
        }
        return okHttpClient
    }

    abstract fun getInterceptor(): Interceptor?
}