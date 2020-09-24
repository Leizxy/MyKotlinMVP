package com.leizy.demo.test2

import android.widget.TextView
import cn.leizy.lib.base.mvp.BaseMvpActivity
import com.alibaba.android.arouter.facade.annotation.Route
import com.leizy.demo.R
import com.leizy.demo.test.TestContract
import kotterknife.bindView

/**
 * @author Created by wulei
 * @date 2020/9/24, 024
 * @description
 */
@Route(path = "/app/test2mvp", extras = 0)
class Test2MvpActivity : BaseMvpActivity<TestContract.View, TestContract.Model, Test2Presenter>(),
    TestContract.View {
    private val tv: TextView by bindView(R.id.tv)
    override fun getLayoutId(): Int {
        return R.layout.activity_test
    }

    override fun initViews() {
        presenter!!.test()
        tv.setOnClickListener { router("/app/jtestmvp").navigation() }
    }

    override fun getParams(key: Int): Map<String, Any> {
        val map: MutableMap<String, Any> = HashMap()
        map.put("int", 1)
        return map
    }

    override fun success(key: Int, obj: Any) {
        when (key) {
            0 -> tv.text = (obj as Int).toString()
        }
    }

    override fun fail(key: Int, obj: Any) {
    }
}