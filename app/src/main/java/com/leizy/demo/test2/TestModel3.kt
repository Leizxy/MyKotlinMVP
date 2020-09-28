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
class TestModel3 : TestContract.Model {
    override fun getTag(): Any {
        return "model2"
    }

    override fun cancelHttp() {
    }

    override fun test(): Int {
        return 0
    }

    override suspend fun test2(params: MutableMap<String, Any>): HttpResponse<LoginBean> {
        Log.i("TestModel3", "test2: ${Thread.currentThread().name}")
        return SuspendApi.getService(SuspendInterface::class.java)
            .login2(RetrofitUtil.getRequestBody(params)).await()
    }

}