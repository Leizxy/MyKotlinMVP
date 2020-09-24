package com.leizy.demo.test

import android.widget.TextView

/**
 * @author Created by wulei
 * @date 2020/9/22, 022
 * @description
 */
class TestModel : TestContract.Model {
    override fun test(): Int {
        return 1210
    }

    override fun getTag(): Any {
        return "tag_test"
    }

    override fun cancelHttp() {

    }
}