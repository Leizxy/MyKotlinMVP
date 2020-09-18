package cn.leizy.lib.retrofit

import cn.leizy.lib.http.IHttp
import cn.leizy.lib.http.bean.HttpResponse
import io.reactivex.Observable
import okhttp3.RequestBody
import org.json.JSONObject
import retrofit2.http.Body
import retrofit2.http.POST

/**
 * @author Created by wulei
 * @date 2020/9/18, 018
 * @description
 */
interface HttpInterface {
    @POST(IHttp.LOGIN)
    fun login(@Body body: RequestBody): Observable<HttpResponse<JSONObject>>
}