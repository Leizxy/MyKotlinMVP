package cn.leizy.lib.base

import android.os.Bundle
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
import com.alibaba.android.arouter.launcher.ARouter

/**
 * @author Created by wulei
 * @date 2019-11-16
 * @description
 */
abstract class BaseActivity : AppCompatActivity() {
    private lateinit var unbinder: Unbinder

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
        MyLiveDataBus.instance.with(
            this.javaClass.simpleName + IHttp.HTTP_TOAST,
            String::class.java
        ).observe(this, Observer { toast(it as String) })
    }

    abstract fun initViews()

    override fun onDestroy() {
        super.onDestroy()
        unbinder.unbind()
        MyLiveDataBus.instance.removeKey(this.javaClass.simpleName + IHttp.HTTP_TOAST)
    }

    fun toast(string: String) {
        ToastUtil.showToast(this, string)
    }

    fun toast(@IdRes idRes: Int) {
        ToastUtil.showToast(this, idRes)
    }
}
