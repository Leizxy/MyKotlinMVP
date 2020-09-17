package cn.leizy.lib.base

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.WindowManager
import androidx.annotation.IdRes
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import butterknife.ButterKnife
import butterknife.Unbinder
import cn.leizy.lib.App
import cn.leizy.lib.http.IHttp
import cn.leizy.lib.util.MyLiveDataBus
import cn.leizy.lib.util.ToastUtil
import com.alibaba.android.arouter.facade.Postcard
import com.alibaba.android.arouter.launcher.ARouter

/**
 * @author Created by wulei
 * @date 2019-11-16
 * @description
 */
abstract class BaseActivity : AppCompatActivity() {
    private lateinit var unbinder: Unbinder

    /**
     * 用于ARouter拦截是否拦截判断依据
     */
    protected var interrupt = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ARouter.getInstance().inject(this)
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)
        setContentView(getLayoutId())
        unbinder = ButterKnife.bind(this)
    }

    abstract fun getLayoutId(): Int


    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        initViews()
    }

    abstract fun initViews()

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        Log.i(this.javaClass.simpleName, "onNewIntent: ")
    }

    override fun onDestroy() {
        super.onDestroy()
        unbinder.unbind()
    }

    fun toast(string: String) {
        ToastUtil.showToast(this, string)
    }

    fun toast(@IdRes idRes: Int) {
        ToastUtil.showToast(this, idRes)
    }

    /**
     * 结合阿里ARouter的一个使用
     * @Required ARouter
     */
    fun router(path: String): Postcard {
        Log.i("BaseActivity", "router: " + interrupt)
        return ARouter.getInstance().build(path).withBoolean("interrupt", interrupt)
    }

    fun router(path: String, group: String): Postcard {
        return ARouter.getInstance().build(path, group).withBoolean("interrupt", interrupt)
    }

    fun router(url: Uri): Postcard {
        return ARouter.getInstance().build(url).withBoolean("interrupt", interrupt)
    }
}
