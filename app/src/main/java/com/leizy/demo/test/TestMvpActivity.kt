package com.leizy.demo.test

import android.widget.TextView
import cn.leizy.lib.base.BaseMvpActivity
import com.alibaba.android.arouter.facade.annotation.Route
import com.leizy.demo.R
import kotterknife.bindView

/**
 * @author Created by wulei
 * @date 2020/9/22, 022
 * @description
 */

@Route(path = "/app/testmvp", extras = 0)
class TestMvpActivity : BaseMvpActivity<TestPresenter>(), TestContract.View {
    private val tv: TextView by bindView(R.id.tv)
    override fun getLayoutId(): Int {
        return R.layout.activity_test
    }

    override fun initViews() {
        presenter!!.test()
        tv.setOnClickListener { router("/app/jtestmvp").navigation() }
    }

    override fun show(str: String) {

    }

    override fun getParams(key: Int): Map<String, Any> {
        val map: MutableMap<String, Any> = HashMap()
        map.put("int", 1)
        return map
    }

    override fun success(key: Int, obj: Any) {
        when (key) {
            0 -> tv.text = obj as String
        }
    }

    override fun fail(key: Int, obj: Any) {
    }
}