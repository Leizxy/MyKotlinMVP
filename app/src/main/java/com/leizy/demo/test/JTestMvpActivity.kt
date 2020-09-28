package com.leizy.demo.test

import android.widget.TextView
import cn.leizy.lib.base.BaseMvpActivity
import cn.leizy.lib.base.JBaseMvpActivity
import com.alibaba.android.arouter.facade.annotation.Route
import com.leizy.demo.R
import kotterknife.bindView

/**
 * @author Created by wulei
 * @date 2020/9/24, 024
 * @description
 */
@Route(path = "/app/jtestmvp", extras = 0)
class JTestMvpActivity : JBaseMvpActivity<JTestPresenter>(), TestContract.View {
    private val tv: TextView by bindView(R.id.tv)
    override fun getLayoutId(): Int {
        return R.layout.activity_jtest
    }

    override fun initViews() {
        presenter.test()
    }

    override fun show(str: String) {

    }

    override fun getParams(key: Int): Map<String, Any> {
        val map: MutableMap<String, Any> = HashMap()
        map.put("int", 2)
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