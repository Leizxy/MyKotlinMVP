package com.leizy.demo.test2

import android.util.Log
import cn.leizy.lib.http.bean.HttpResponse
import cn.leizy.lib.http.bean.LoginBean
import cn.leizy.lib.http.bean.TestObj
import cn.leizy.lib.retrofit.RetrofitUtil
import cn.leizy.lib.retrofit.SuspendApi
import cn.leizy.lib.retrofit.SuspendInterface
import com.leizy.demo.test.TestContract

/**
 * @author Created by wulei
 * @date 2020/9/28, 028
 * @description
 */
class TestModel2 : CoroutinesModel(), TestContract.Model {
    override fun getTag(): Any {
        return "model2"
    }

    override fun test(): Int {
        val params: MutableMap<String, Any> = HashMap()
        params["Moblie"] = "13245678978"
        params["MT"] = ""
        params["Password"] = "TmkuQqmJvuc\u003d"
        params["Regid"] = "13065ffa4e915b84c29"
        launchRequest(
            {
                SuspendApi.getService(SuspendInterface::class.java)
                    .login2(RetrofitUtil.getRequestBody(params)).await()
            }, {
                Log.i("TestModel2", "test: ")
            }/*, { err: String? ->
                Log.i("TestModel2", "test: $err")
            }*/)
        return 0
    }

    override suspend fun test2(params: MutableMap<String, Any>): HttpResponse<LoginBean>? {
        return null
    }

}