package cn.leizy.lib.http.retrofit

import android.app.Application
import cn.leizy.lib.App
import cn.leizy.lib.BuildConfig
import cn.leizy.lib.http.BaseCallback
import cn.leizy.lib.http.IHttp
import cn.leizy.lib.http.IHttp.Companion.DEFAULT_MILLISECONDS
import com.google.gson.Gson
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.util.concurrent.TimeUnit

/**
 * @author Created by wulei
 * @date 2020/8/27, 027
 * @description
 */
class RetrofitHttp : IHttp {
    private var context: Application? = null
    private var retrofit: Retrofit? = null
    private var api: RetrofitApi? = null
    private var client: OkHttpClient? = null

    private fun getOkHttpClient(): OkHttpClient {
        if (client != null) {
            return client as OkHttpClient
        }
        val builder: OkHttpClient.Builder = OkHttpClient.Builder()
        val logging = HttpLoggingInterceptor()
        if (BuildConfig.DEBUG) {
            logging.level = HttpLoggingInterceptor.Level.BODY
        } else {
            logging.level = HttpLoggingInterceptor.Level.BASIC
        }
        builder.addInterceptor(logging)

        builder.readTimeout(DEFAULT_MILLISECONDS, TimeUnit.MILLISECONDS)
        builder.writeTimeout(DEFAULT_MILLISECONDS, TimeUnit.MILLISECONDS)
        builder.connectTimeout(DEFAULT_MILLISECONDS, TimeUnit.MILLISECONDS)
        client = builder.build()
        return client!!
    }

    override fun initHttp(context: Application) {
        this.context = context
        retrofit = Retrofit.Builder()
            .baseUrl(IHttp.HOST)
            .addConverterFactory(GsonConverterFactory.create())
            .client(getOkHttpClient())
            .build()
        api = retrofit!!.create(RetrofitApi::class.java)
    }

    override fun <T> get(url: String, params: Map<String, Any>?, callback: BaseCallback<T>) {
        TODO("Not yet implemented")
    }

    override fun <T> post(
        url: String,
        tag: Any,
        params: Map<String, Any>?,
        callback: BaseCallback<T>
    ) {
        //处理params为json放入post请求request的body中
        val body: RequestBody
        if (params != null) {
            body =
                Gson().toJson(params).toRequestBody("application/json;charset=utf-8".toMediaType())
        } else {
            body = "".toRequestBody("application/json;charset=utf-8".toMediaType())
        }
        //处理token
        val token: String? = "test"
        val call: Call<T>
        if (token == null) {
            call = api!!.post(url, body, tag)
        } else {
            call = api!!.post(url, body, token, tag)
        }
        call.enqueue(object : Callback<T> {
            override fun onResponse(call: Call<T>, response: Response<T>) {
                callback.onSuccess(response.body()!!)
            }

            override fun onFailure(call: Call<T>, t: Throwable) {
                if (t is SocketTimeoutException) {
                    App.getInstance().toast("connect time out")
                } else if (t is ConnectException) {
                    App.getInstance().toast("connect failed")
                } else {
                    callback.onFail(t)
                }
            }
        })
    }

    override fun cancel(tag: Any) {
    }

    override fun addHeader(key: String, value: String) {
        TODO("Not yet implemented")
    }
}