package cn.leizy.lib.retrofit

import cn.leizy.lib.http.IHttp
import cn.leizy.lib.http.bean.HttpResponse
import cn.leizy.lib.http.bean.LoginBean
import cn.leizy.lib.http.bean.TestObj
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Job
import okhttp3.RequestBody
import org.json.JSONObject
import retrofit2.http.Body
import retrofit2.http.POST

/**
 * @author Created by wulei
 * @date 2020/9/22, 022
 * @description
 */
interface SuspendInterface {
    @POST(IHttp.LOGIN)
    suspend fun login(@Body body: RequestBody): HttpResponse<JSONObject>
    @POST(IHttp.LOGIN)
    fun login1(@Body body: RequestBody): Deferred<HttpResponse<LoginBean>>
    @POST(IHttp.LOGIN)
    fun login2(@Body body: RequestBody): Deferred<HttpResponse<LoginBean>>
}