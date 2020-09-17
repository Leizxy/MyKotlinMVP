package cn.leizy.login

import android.os.Bundle
import android.util.Log
import android.view.View
import cn.leizy.lib.base.BaseActivity
import cn.leizy.lib.http.bean.HttpResponse
import com.alibaba.android.arouter.facade.annotation.Autowired
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter

@Route(path = "/login/test")
class LoginActivity : BaseActivity() {
    @Autowired
    @JvmField
    var obj: HttpResponse<String>? = null

    @Autowired
    @JvmField
    var bool: Boolean? = false

    @Autowired
    @JvmField
    var l: Long? = 0

    @Autowired
    @JvmField
    var test: Int? = 0

    override fun getLayoutId(): Int {
        ARouter.getInstance().inject(this)
        return R.layout.activity_login
    }

    override fun initViews() {
        Log.i("LoginActivity", "initViews: ${intent.extras!!.getInt("test")}")
        Log.i("LoginActivity", "initViews: ${obj!!.Result}")
        Log.i("LoginActivity", "initViews: $bool")
        Log.i("LoginActivity", "initViews: $l")
        Log.i("LoginActivity", "initViews: $test")
    }

    fun login(view: View) {
        router("/app/main").withBoolean("fromLogin",true).navigation()
    }
}
