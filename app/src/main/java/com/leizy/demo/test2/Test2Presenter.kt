package com.leizy.demo.test2

import android.util.Log
import cn.leizy.lib.base.mvp.BasePresenter
import com.leizy.demo.test.TestContract
import com.leizy.demo.test.TestModel
import kotlinx.coroutines.*

/**
 * @author Created by wulei
 * @date 2020/9/24, 024
 * @description
 */
class Test2Presenter : BasePresenter<TestContract.View, TestContract.Model>(),
    TestContract.Presenter {

    override fun createModel(): TestContract.Model? {
        return TestModel()
    }

    override fun test() {
        Log.i("Test2Presenter", "test: ${isViewAttached}")
        GlobalScope.launch(Dispatchers.Main) {
            delay(1000)
            view!!.success(0, withContext(Dispatchers.IO) {
                model!!.test()
            })
        }
    }
}