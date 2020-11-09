package cn.leizy.lib.base.mvp

import android.os.Bundle
import cn.leizy.lib.base.BaseActivity
import java.lang.Exception
import java.lang.RuntimeException
import java.lang.reflect.ParameterizedType

/**
 * @author Created by wulei
 * @date 2020/9/24, 024
 * @description 适用kotlin
 */
abstract class BaseMvpActivity<V, M, P : IPresenter<V, M>> : BaseActivity(), IView {
    protected var presenter: P? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        presenter = getPresenter()
        presenter!!.attachView(this as V)
        super.onCreate(savedInstanceState)
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter!!.detachView()
    }

    @Suppress("UNCHECKED_CAST")
    @JvmName("getPresenter1")
    private fun getPresenter(): P? {
        val p: P
        try {
            val pt: ParameterizedType = this.javaClass.genericSuperclass as ParameterizedType
            val clazz: Class<P> = pt.actualTypeArguments[2] as Class<P>
            p = clazz.newInstance()
        } catch (e: Exception) {
            throw RuntimeException(e)
        }
        return p
    }

    override fun showToast(str: String?) {

    }

    override fun showToast(stringId: Int) {
    }
}