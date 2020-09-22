package com.leizy.demo.test

import cn.leizy.lib.base.BaseMvpActivity
import com.alibaba.android.arouter.facade.annotation.Route
import com.leizy.demo.R

/**
 * @author Created by wulei
 * @date 2020/9/22, 022
 * @description
 */

@Route(path = "/app/testmvp", extras = 0)
class TestMvpActivity:BaseMvpActivity<TestPresenter>(),TestContract.View {
    override fun getLayoutId(): Int {
        return R.layout.activity_test
    }

    override fun initViews() {
    }

    override fun getParams(key: Int): Map<String, Any> {
        return HashMap()
    }

    override fun success(key: Int, obj: Any) {
    }

    override fun fail(key: Int, obj: Any) {
    }
}