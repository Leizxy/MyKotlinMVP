package cn.leizy.lib.retrofit

import cn.leizy.lib.http.IHttp
import cn.leizy.lib.http.bean.HttpResponse
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
}