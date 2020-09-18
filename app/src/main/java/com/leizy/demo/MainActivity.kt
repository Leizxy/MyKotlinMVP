package com.leizy.demo

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import cn.leizy.lib.base.BaseActivity
import cn.leizy.lib.http.HttpProxy
import cn.leizy.lib.http.IHttp
import cn.leizy.lib.http.bean.HttpResponse
import cn.leizy.lib.http.okgo.OkGoCallback
import cn.leizy.lib.http.retrofit.RetrofitApi
import cn.leizy.lib.retrofit.CommonApi
import cn.leizy.lib.retrofit.HttpInterface
import cn.leizy.lib.retrofit.RetrofitUtil
import cn.leizy.lib.router.service.HelloServiceImpl
import com.alibaba.android.arouter.facade.Postcard
import com.alibaba.android.arouter.facade.annotation.Autowired
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.facade.callback.NavigationCallback
import com.alibaba.android.arouter.launcher.ARouter
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import org.json.JSONObject
import org.reactivestreams.Subscriber
import org.reactivestreams.Subscription

@Route(path = "/app/main", extras = 0)
class MainActivity : BaseActivity() {
    @Autowired
    @JvmField
    var l: Long = 0
    override fun getLayoutId(): Int {
        return R.layout.activity_main
    }

    override fun initViews() {
        Log.i("MainActivity", "initViews: long var is $l")
        interrupt = l <= -1

        Log.i("MainActivity", "initViews: ${Test().test()}")
    }

    fun click(view: View) {
//        ARouter.getInstance().build("/login/test").navigation()

        val params: MutableMap<String, Any> = HashMap()
        params["Moblie"] = "13245678978"
        params["MT"] = ""
        params["Password"] = "TmkuQqmJvuc\u003d"
        params["Regid"] = "13065ffa4e915b84c29"

        HttpProxy.get().post(HttpProxy.buildUrl(IHttp.LOGIN), "main", params, object :
            OkGoCallback<HttpResponse<Any>>() {
            override fun onSuccess(body: HttpResponse<Any>) {
                val obj: HttpResponse<String> = HttpResponse()
                obj.Result = "test"
/*                val bundle = Bundle()
                bundle.putInt("test", 111)*/
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

        // TODO: 2020/9/18, 018  
        CommonApi.getService(HttpInterface::class.java).login(RetrofitUtil.getRequestBody(params))
            .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe()
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        l = intent!!.extras!!["l"] as Long
        Log.i("MainActivity", "onNewIntent: $l")
        interrupt = false
        Log.i("MainActivity", "onNewIntent: $interrupt")
    }
}
