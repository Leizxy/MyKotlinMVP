package cn.leizy.lib.base

import android.os.Bundle
import java.lang.Exception
import java.lang.RuntimeException
import java.lang.reflect.ParameterizedType

/**
 * @author Created by wulei
 * @date 2019-11-18
 * @description
 */
abstract class BaseMvpActivity<P : BasePresenter> : BaseActivity(), IView {
    private var presenter: P? = null
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

    private fun getPresenter(): P {
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