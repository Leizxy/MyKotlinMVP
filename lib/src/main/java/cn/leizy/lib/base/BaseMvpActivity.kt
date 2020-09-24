package cn.leizy.lib.base

import android.os.Bundle
import cn.leizy.lib.base.mvp.IView
import java.lang.Exception
import java.lang.RuntimeException
import java.lang.reflect.ParameterizedType

/**
 * @author Created by wulei
 * @date 2019-11-18
 * @description 不太好用。建议使用java版的BaseMvpActivity别继承该类。
 */
abstract class BaseMvpActivity<P : BasePresenter> : BaseActivity(), IView {
    protected var presenter: P? = getPresenter()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        presenter = getPresenter()
        presenter?.attachView(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter?.detachView()
        presenter = null
    }

    @JvmName("getPresenter1")
    protected fun getPresenter(): P {
        val p: P
        try {
            val pt: ParameterizedType = javaClass.genericSuperclass as ParameterizedType
            val clazz: Class<*> = pt.actualTypeArguments[0] as Class<*>
            @Suppress("UNCHECKED_CAST")
            p = clazz.newInstance() as P
        } catch (e: Exception) {
            throw RuntimeException(e)
        }
        return p
    }
}