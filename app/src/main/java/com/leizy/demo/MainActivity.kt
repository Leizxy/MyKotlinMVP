package com.leizy.demo

import android.view.View
import cn.leizy.lib.base.BaseActivity
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter

@Route(path = "/app/main")
class MainActivity : BaseActivity() {
    override fun getLayoutId(): Int {
        return R.layout.activity_main
    }

    override fun initViews() {
    }

    fun click(view: View) {
        ARouter.getInstance().build("/login/test").navigation()
    }
}
