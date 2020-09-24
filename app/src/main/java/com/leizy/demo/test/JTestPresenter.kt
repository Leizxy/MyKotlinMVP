package com.leizy.demo.test

import android.util.Log
import cn.leizy.lib.base.JBasePresenter
import kotlinx.coroutines.*

/**
 * @author Created by wulei
 * @date 2020/9/22, 022
 * @description
 */
class JTestPresenter : JBasePresenter<TestContract.View, TestContract.Model>(),
    TestContract.Presenter {
    override fun createModel(): TestContract.Model {
        return TestModel()
    }

    override fun test() {
        Log.i("TestPresenter", "test: ${isViewAttached}")
        GlobalScope.launch(Dispatchers.Main) {
            delay(1000)
            view!!.success(0, withContext(Dispatchers.IO) {
                model.test()
            })
        }
    }
}