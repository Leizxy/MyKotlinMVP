package cn.leizy.lib

import android.app.Activity
import android.app.ActivityManager
import android.app.Application
import android.content.ComponentName
import android.content.Context
import android.os.Bundle
import android.util.Log
import cn.leizy.lauch.TaskDispatcher
import cn.leizy.lib.http.HttpProxy
import cn.leizy.lib.tasks.InitARouter
import cn.leizy.lib.util.ToastUtil
import cn.leizy.lib.tasks.InitHttp
import cn.leizy.lib.tasks.InitLeak
import cn.leizy.lib.util.Timing
import com.alibaba.android.arouter.launcher.ARouter
import java.lang.ref.WeakReference

/**
 * @author Created by wulei
 * @date 2019-11-11
 * @description
 */
class App : Application(), Application.ActivityLifecycleCallbacks {
    private var currentActivity: Activity? = null

    companion object {
        private var instance: App? = null

        fun getInstance(): App {
            return instance!!
        }
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
        if (BuildConfig.DEBUG) {
            registerActivityLifecycleCallbacks(this)
        }
        Timing.startRecord()
        TaskDispatcher.init(this)
        val dispatcher = TaskDispatcher.createInstance()
        dispatcher.addTask(InitHttp())
            .addTask(InitARouter())
            .addTask(InitLeak())
            .start()
        dispatcher.await()
        ToastUtil.init(this)
        Timing.endRecord("App init")
    }

    override fun onTerminate() {
        super.onTerminate()
        ARouter.getInstance().destroy()
    }

    fun toast(str: String?) {
        ToastUtil.showToast(str)
    }

    fun getCurrentStr(): String {
        return currentActivity!!.javaClass.simpleName
    }

    override fun onActivityPaused(p0: Activity?) {
        Log.i("App", "onActivityPaused" + p0?.javaClass!!.simpleName)
    }

    override fun onActivityResumed(p0: Activity?) {
        Log.i("App", "onActivityResumed: " + p0?.javaClass!!.simpleName)
    }

    override fun onActivityStarted(p0: Activity?) {
        currentActivity = p0
        Log.i("App", "onActivityStarted: " + p0?.javaClass!!.simpleName)
    }

    override fun onActivityDestroyed(p0: Activity?) {
        Log.i("App", "onActivityDestroyed: " + p0?.javaClass!!.simpleName)
    }

    override fun onActivitySaveInstanceState(p0: Activity?, p1: Bundle?) {
        Log.i("App", "onActivitySaveInstanceState: " + p0?.javaClass!!.simpleName)
    }

    override fun onActivityStopped(p0: Activity?) {
        Log.i("App", "onActivityStopped: " + p0?.javaClass!!.simpleName)
    }

    override fun onActivityCreated(p0: Activity?, p1: Bundle?) {
        Log.i("App", "onActivityCreated: " + p0?.javaClass!!.simpleName)
    }
}
