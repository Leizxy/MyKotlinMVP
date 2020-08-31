package com.leizy.demo

import android.view.View
import cn.leizy.lib.base.BaseActivity
import cn.leizy.lib.http.BaseCallback
import cn.leizy.lib.http.HttpProxy
import cn.leizy.lib.http.IHttp
import cn.leizy.lib.http.bean.HttpResponse
import cn.leizy.lib.http.okgo.OkGoCallback
import com.alibaba.android.arouter.facade.annotation.Route
import java.util.*

@Route(path = "/app/main")
class MainActivity : BaseActivity() {
    override fun getLayoutId(): Int {
        return R.layout.activity_main
    }

    override fun initViews() {
    }

    fun click(view: View) {
//        ARouter.getInstance().build("/login/test").navigation()

        val params: MutableMap<String, String> = HashMap()
        params["Moblie"] = "13245678978"
        params["MT"] = ""
        params["Password"] = "TmkuQqmJvuc\u003d"
        params["Regid"] = "13065ffa4e915b84c29"

        HttpProxy.get().post(HttpProxy.buildUrl(IHttp.LOGIN), "main", params, object :
            OkGoCallback<HttpResponse<Any>>() {
            override fun onSuccess(body: HttpResponse<Any>) {
            }
        })
    }
}
