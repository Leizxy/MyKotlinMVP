package com.leizy.demo

import android.util.Log
import android.view.View
import cn.leizy.lib.base.BaseActivity
import cn.leizy.lib.http.HttpProxy
import cn.leizy.lib.http.IHttp
import cn.leizy.lib.http.bean.HttpResponse
import cn.leizy.lib.http.okgo.OkGoCallback
import cn.leizy.lib.router.service.HelloServiceImpl
import com.alibaba.android.arouter.facade.Postcard
import com.alibaba.android.arouter.facade.annotation.Autowired
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.facade.callback.NavigationCallback
import com.alibaba.android.arouter.launcher.ARouter
import java.util.*

@Route(path = "/app/main", extras = 0)
class MainActivity : BaseActivity() {
    @Autowired
    @JvmField
    var l: Long = 0
    override fun getLayoutId(): Int {
        ARouter.getInstance().inject(this)
        return R.layout.activity_main
    }

    override fun initViews() {
        Log.i("MainActivity", "initViews: long var is $l")
        interrupt = l <= 0

        Log.i("MainActivity", "initViews: ${Test().test()}")
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
                val obj: HttpResponse<String> = HttpResponse()
                obj.Result = "test"

                router("/login/test")
                    .withLong("long", 32)
                    .withBoolean("bool", interrupt)
                    .withObject("obj", obj)
                    .navigation(this@MainActivity, object : NavigationCallback {
                        override fun onFound(postcard: Postcard?) {
                            Log.i("MainActivity", "onFound: $l")
                        }

                        override fun onLost(postcard: Postcard?) {
                            Log.i("MainActivity", "onLost: $l")
                        }

                        override fun onArrival(postcard: Postcard?) {
                            Log.i("MainActivity", "onArrival: $l")
                        }

                        override fun onInterrupt(postcard: Postcard?) {
                            Log.i("MainActivity", "onInterrupt: $l")
                        }
                    })
            }
        })
    }
}
