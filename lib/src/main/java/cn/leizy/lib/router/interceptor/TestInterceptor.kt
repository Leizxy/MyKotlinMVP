package cn.leizy.lib.router.interceptor

import android.content.Context
import android.util.Log
import cn.leizy.lib.BuildConfig
import com.alibaba.android.arouter.facade.Postcard
import com.alibaba.android.arouter.facade.annotation.Interceptor
import com.alibaba.android.arouter.facade.callback.InterceptorCallback
import com.alibaba.android.arouter.facade.template.IInterceptor
import com.alibaba.android.arouter.launcher.ARouter

/**
 * @author Created by wulei
 * @date 2020/9/4, 004
 * @description
 */
@Interceptor(priority = 8, name = "test")
class TestInterceptor : IInterceptor {
    override fun init(context: Context?) {

    }

    override fun process(postcard: Postcard?, callback: InterceptorCallback?) {
        val bundle = postcard!!.extras
        if (bundle.getBoolean("interrupt")) {
            ARouter.getInstance().build("/app/main")
                .withLong("l", 11).navigation()
            callback!!.onInterrupt(Throwable("test interrupt"))
            return
        } else {
            callback!!.onContinue(postcard)
        }
        if (BuildConfig.DEBUG) {
            Log.i("ARouter", "targetActivity : ${postcard.destination}")
        }
    }
}