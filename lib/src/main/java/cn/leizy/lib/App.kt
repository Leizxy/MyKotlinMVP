package cn.leizy.lib

import android.app.Activity
import android.app.Application
import android.os.Bundle
import android.util.Log
import cn.leizy.lib.http.HttpProxy

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
        registerActivityLifecycleCallbacks(this)
        HttpProxy.init(this)
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
