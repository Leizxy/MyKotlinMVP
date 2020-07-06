package cn.leizy.lib.http.okgo

import android.app.Application
import com.lzy.okgo.OkGo
import com.lzy.okgo.cache.CacheEntity
import com.lzy.okgo.cache.CacheMode
import com.lzy.okgo.interceptor.HttpLoggingInterceptor
import com.lzy.okgo.model.HttpHeaders
import com.lzy.okgo.model.Response
import com.lzy.okgo.request.PostRequest
import com.lzy.okgo.request.base.Request
import cn.leizy.lib.http.IHttp
import okhttp3.OkHttpClient
import org.json.JSONObject
import java.lang.StringBuilder
import java.lang.reflect.ParameterizedType
import java.util.concurrent.TimeUnit
import java.util.logging.Level

/**
 * @author Created by wulei
 * @date 2019-11-11
 * @description
 */
class OkGoHttp : IHttp {
    private var context: Application? = null
    override fun initHttp(context: Application) {
        this.context = context
        val builder: OkHttpClient.Builder = OkHttpClient.Builder()
        val logging = HttpLoggingInterceptor("OkGo")
        logging.setPrintLevel(HttpLoggingInterceptor.Level.BODY)
        logging.setColorLevel(Level.WARNING)
        builder.addInterceptor(logging)

        builder.readTimeout(OkGo.DEFAULT_MILLISECONDS, TimeUnit.MILLISECONDS)
        builder.writeTimeout(OkGo.DEFAULT_MILLISECONDS, TimeUnit.MILLISECONDS)
        builder.connectTimeout(OkGo.DEFAULT_MILLISECONDS, TimeUnit.MILLISECONDS)
        val header = HttpHeaders()
        header.put("Content-Type: application/json", "Accept: application/json")
        OkGo.getInstance().init(context)
            .setOkHttpClient(builder.build())               //建议设置OkHttpClient，不设置会使用默认的
            .setCacheMode(CacheMode.NO_CACHE)               //全局统一缓存模式，默认不使用缓存，可以不传
            .setCacheTime(CacheEntity.CACHE_NEVER_EXPIRE)   //全局统一缓存时间，默认永不过期，可以不传
            .setRetryCount(3)                               //全局统一超时重连次数，默认为三次，那么最差的情况会请求4次(一次原始请求，三次重连请求)，不需要可以设置为0
//                .addCommonHeaders(headers)
    }

    override fun <T> get(url: String, params: Map<String, Any>?, callback: NetCallback<T>) {
        val type = callback.javaClass.genericSuperclass
        val type1 = (type as ParameterizedType).actualTypeArguments[0]
        val stringBuilder = StringBuilder(url)
        if (params != null) {
            stringBuilder.append("?")
            val iterator = params.entries.iterator()
            while (iterator.hasNext()) {
                val entry = iterator.next() as Map.Entry<*, *>
                stringBuilder.append(entry.key)
                stringBuilder.append("=")
                stringBuilder.append(entry.value)
                stringBuilder.append("&")
            }
            stringBuilder.deleteCharAt(stringBuilder.length - 1)
        }
        val getRequest = OkGo.get<T>(stringBuilder.toString())
        //TODO add token
        //getRequest.headers("key", "")
//        var headerToken
        getRequest.execute(object : MyCallback<T>(type1) {
            override fun onStart(request: Request<T, out Request<Any, Request<*, *>>>?) {
                super.onStart(request)
                callback.onStart()
            }

            override fun onSuccess(response: Response<T>) {
                //todo 此处可处理特定http返回，如401，等
                callback.onSuccess(response.body())
            }

            override fun onError(response: Response<T>) {
                super.onError(response)
                callback.onFail(response.exception)
            }

            override fun onFinish() {
                super.onFinish()
                callback.onFinish()
            }

        })

    }

    override fun <T> post(url: String, tag: Any, params: Map<String, Any>?, callback: NetCallback<T>) {
        val type = callback.javaClass.genericSuperclass
        val type1 = (type as ParameterizedType).actualTypeArguments[0]
        val postRequest: PostRequest<T> = OkGo.post<T>(url).tag(tag)
        if (params != null) {
            val jsonStr = JSONObject(params).toString()
            postRequest.upJson(jsonStr)
        }
        postRequest.execute(object : MyCallback<T>(type1) {
            override fun onStart(request: Request<T, out Request<Any, Request<*, *>>>?) {
                super.onStart(request)
                callback.onStart()
            }

            override fun onSuccess(response: Response<T>) {
                callback.onSuccess(response.body())
            }

            override fun onError(response: Response<T>) {
                super.onError(response)
                callback.onFail(response.exception)
            }

            override fun onFinish() {
                super.onFinish()
                callback.onFinish()
            }

        })
    }

    override fun cancel(tag: Any) {
        OkGo.getInstance().cancelTag(tag)
    }

    override fun addHeader(key: String, value: String) {
        val headers = HttpHeaders()
        headers.put(key, value)
        OkGo.getInstance().addCommonHeaders(headers)
    }

}