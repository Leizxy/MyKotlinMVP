package com.leizy.demo

import android.content.Intent
import android.util.Log
import android.view.View
import android.widget.TextView
import cn.leizy.lib.base.BaseActivity
import cn.leizy.lib.retrofit.RetrofitUtil
import cn.leizy.lib.retrofit.SuspendApi
import cn.leizy.lib.retrofit.SuspendInterface
import com.alibaba.android.arouter.facade.annotation.Autowired
import com.alibaba.android.arouter.facade.annotation.Route
import kotlinx.coroutines.*
import kotterknife.bindView

@Route(path = "/app/main", extras = 0)
class MainActivity : BaseActivity(), CoroutineScope by MainScope() {
    //    @BindView(R.id.tv) lateinit var tv:TextView
    private val tv: TextView by bindView(R.id.tv)

    @Autowired
    @JvmField
    var l: Long = 0
    override fun getLayoutId(): Int {
        return R.layout.activity_main
    }

    private lateinit var job: Job
    private var change: Boolean = false

    override fun initViews() {
        Log.i("MainActivity", "initViews: long var is $l")
        interrupt = l <= -1

        Log.i("MainActivity", "initViews: ${Test().test()}")

        job = GlobalScope.launch(Dispatchers.Main) {
            Log.i("MainActivity", "initViews: launch ${System.currentTimeMillis()}")
            delay(1000)
            Log.i("MainActivity", "initViews: launch delay ${System.currentTimeMillis()}")
            tv.setText("haha coroutine")
            tv.text = async(Dispatchers.Main, CoroutineStart.DEFAULT, block = {
                delay(1000)
                getText()
            }).await()
        }
    }

    suspend fun getText(): String {
        return "suspend text"
    }


    fun click(view: View) {
//        ARouter.getInstance().build("/login/test").navigation()

        val params: MutableMap<String, Any> = HashMap()
        params["Moblie"] = "13245678978"
        params["MT"] = ""
        params["Password"] = "TmkuQqmJvuc\u003d"
        params["Regid"] = "13065ffa4e915b84c29"

        /*HttpProxy.get().post(HttpProxy.buildUrl(IHttp.LOGIN), "main", params, object :
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
        })*/

        // TODO: 2020/9/18, 018
/*        CommonApi.getService(HttpInterface::class.java).login(RetrofitUtil.getRequestBody(params))
            .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                Consumer { Log.i("MainActivity", "next: ${it.IsSuccess}") },
                Consumer { Log.i("MainActivity", "throwable: ${it.message}") },
                Action { Log.i("MainActivity", "action: ") }
            )*/
        router("/app/test2mvp").navigation()
        return

        GlobalScope.launch {
            Log.i("MainActivity", "launch ${Thread.currentThread().name}")
            val login = SuspendApi.getService(SuspendInterface::class.java)
                .login(RetrofitUtil.getRequestBody(params))
            Log.i("MainActivity", "launch ${login.IsSuccess}")
        }

        Log.i("MainActivity", "click: ")

        GlobalScope.launch(Dispatchers.Main) {
//            Log.i("MainActivity", "await: ${Thread.currentThread().name}")
            tv.text = withContext(Dispatchers.IO) {
//                Log.i("MainActivity", "await: ${Thread.currentThread().name}")
                SuspendApi.getService(SuspendInterface::class.java)
                    .login(RetrofitUtil.getRequestBody(params))
            }.OperationDesc
//            Log.i("MainActivity", "await: ${await.IsSuccess}")
//            tv.setText(await.OperationDesc)
        }
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        l = intent!!.extras!!["l"] as Long
        Log.i("MainActivity", "onNewIntent: $l")
        interrupt = false
        Log.i("MainActivity", "onNewIntent: $interrupt")
    }
}
