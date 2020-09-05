package cn.leizy.lib.http.retrofit

import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*

/**
 * @author Created by wulei
 * @date 2020/8/27, 027
 * @description
 */
interface RetrofitApi {
    @POST
    fun <T> post(@Url url: String, @Body bodyRequest: RequestBody, @Tag tag: Any): Call<T>

    @POST
    fun <T> post(
        @Url url: String,
        @Body bodyRequest: RequestBody,
        @Header("AccessCode") token: String,
        @Tag tag: Any
    ): Call<T>
}