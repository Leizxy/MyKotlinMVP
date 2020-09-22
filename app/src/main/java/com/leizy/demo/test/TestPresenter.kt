package com.leizy.demo.test

import cn.leizy.lib.base.BasePresenter
import cn.leizy.lib.base.IModel

/**
 * @author Created by wulei
 * @date 2020/9/22, 022
 * @description
 */
class TestPresenter : BasePresenter(),
    TestContract.Presenter {
    override fun createModel(): TestContract.Model {
        return TestModel()
    }
}