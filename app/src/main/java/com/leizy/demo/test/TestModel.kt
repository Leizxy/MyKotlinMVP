package com.leizy.demo.test

import android.widget.TextView
import cn.leizy.lib.http.bean.HttpResponse
import cn.leizy.lib.http.bean.LoginBean
import cn.leizy.lib.http.bean.TestObj

/**
 * @author Created by wulei
 * @date 2020/9/22, 022
 * @description
 */
class TestModel : TestContract.Model {
    override fun test(): Int {
        return 1210
    }

    override suspend fun test2(params: MutableMap<String, Any>): HttpResponse<LoginBean>? {
        return null
    }

    override fun getTag(): Any {
        return "tag_test"
    }

    override fun cancelHttp() {

    }
}