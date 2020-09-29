package com.leizy.demo.test

import android.util.Log
import cn.leizy.lib.base.BasePresenter
import cn.leizy.lib.base.mvp.IModel
import cn.leizy.lib.base.mvp.IView
import kotlinx.coroutines.*

/**
 * @author Created by wulei
 * @date 2020/9/22, 022
 * @description
 */
class TestPresenter<V : IView, M : IModel> : BasePresenter<TestContract.View, TestContract.Model>(),
    TestContract.Presenter {
    override fun createModel(): TestContract.Model {
        return TestModel()
    }

    override fun test() {
        Log.i("TestPresenter", "test: ${isViewAttached}")
        GlobalScope.launch(Dispatchers.Main) {
            delay(1000)
            view?.success(0, withContext(Dispatchers.IO) {
                (model as TestModel).test()
            })
        }
    }
}