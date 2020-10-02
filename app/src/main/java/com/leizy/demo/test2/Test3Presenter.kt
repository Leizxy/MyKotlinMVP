package com.leizy.demo.test2

import android.util.Log
import cn.leizy.lib.base.mvp.BaseCoroutinePresenter
import cn.leizy.lib.http.bean.LoginBean
import com.leizy.demo.test.TestContract
import kotlinx.coroutines.*

/**
 * @author Created by wulei
 * @date 2020/9/24, 024
 * @description
 */
class Test3Presenter : BaseCoroutinePresenter<TestContract.View, TestContract.Model>(),
    TestContract.Presenter {

    override fun createModel(): TestContract.Model? {
        return TestModel3()
    }

    override fun test() {
        val params: MutableMap<String, Any> = HashMap()
        params["Moblie"] = "18010665679"
        params["MT"] = ""
        params["Password"] = "UHSCma5T8aM\u003d"
        params["Regid"] = "13065ffa4e915b84c29"
        launchRequest(
            {
                view!!.show("start")
                model!!.test2(params)
            }, { bean: LoginBean? ->
                Log.i("TestModel2", "test: ${bean!!.Token}")
                launchMain {
                    view!!.show(bean.Token!!)
                }
            }/*, { err: String? ->
                Log.i("TestModel2", "test: $err")
            }*//*, failBlock = {
                launchMain {
                    view!!.show("finally")
                }
            }*/
        )
    }
}