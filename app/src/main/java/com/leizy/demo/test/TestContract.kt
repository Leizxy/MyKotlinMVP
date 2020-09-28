package com.leizy.demo.test

import cn.leizy.lib.base.mvp.IModel
import cn.leizy.lib.base.mvp.IView
import cn.leizy.lib.http.bean.HttpResponse
import cn.leizy.lib.http.bean.LoginBean
import cn.leizy.lib.http.bean.TestObj
import com.leizy.demo.test2.CoroutinesModel
import kotlinx.coroutines.Deferred

/**
 * @author Created by wulei
 * @date 2020/9/22, 022
 * @description
 */
interface TestContract {
    interface Model : IModel {
        fun test(): Int
        suspend fun test2(params: MutableMap<String, Any>): HttpResponse<LoginBean>?
    }

    interface View : IView {
        fun show(str: String)
    }

    interface Presenter {
        fun test()
    }
}