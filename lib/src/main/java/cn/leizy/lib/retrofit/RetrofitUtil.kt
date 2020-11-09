package cn.leizy.lib.retrofit

import com.google.gson.Gson
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody

/**
 * @author Created by wulei
 * @date 2020/9/18, 018
 * @description
 */
object RetrofitUtil {
    fun getRequestBody(params: MutableMap<String, Any>):RequestBody {
        return RequestBody.create("application/json;charset=UTF-8".toMediaTypeOrNull(), Gson().toJson(params).toString())
    }
}