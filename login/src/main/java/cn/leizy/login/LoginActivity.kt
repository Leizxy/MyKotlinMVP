package cn.leizy.login

import cn.leizy.lib.base.BaseActivity
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter

@Route(path = "/login/test")
class LoginActivity : BaseActivity() {

    override fun getLayoutId(): Int {
        ARouter.getInstance().inject(this)
        return R.layout.activity_login
    }

    override fun initViews() {

    }
}
