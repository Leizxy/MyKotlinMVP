package com.leizy.demo.test

import cn.leizy.lib.base.mvp.IModel
import cn.leizy.lib.base.mvp.IView

/**
 * @author Created by wulei
 * @date 2020/9/22, 022
 * @description
 */
interface TestContract {
    interface Model : IModel {
        fun test(): Int
    }

    interface View : IView {
    }

    interface Presenter {
        fun test()
    }
}