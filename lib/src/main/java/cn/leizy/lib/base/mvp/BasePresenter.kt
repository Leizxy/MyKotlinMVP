package cn.leizy.lib.base.mvp

import java.lang.ref.WeakReference
import java.lang.reflect.InvocationHandler
import java.lang.reflect.Method
import java.lang.reflect.Proxy

/**
 * @author Created by wulei
 * @date 2020/9/24, 024
 * @description
 */
@Suppress("UNCHECKED_CAST")
abstract class BasePresenter<V, M : IModel> : IPresenter<V, M> {
    protected var view: V? = null
    protected var model: M? = null
    private var weakReference: WeakReference<V>? = null
    protected var isViewAttached: Boolean = weakReference != null && weakReference?.get() != null
    override fun attachView(view: V) {
        weakReference = WeakReference(view)
        this.view = Proxy.newProxyInstance(
            view!!::class.java.classLoader,
            view!!::class.java.interfaces,
            MvpViewHandler(weakReference?.get()!!)
        ) as V
        if (this.model == null) {
            this.model = createModel()
        }
    }

    abstract fun createModel(): M?

    override fun detachView() {
        model!!.cancelHttp()
        this.model = null
        if (isViewAttached) {
            weakReference!!.clear()
            weakReference = null
        }
    }

    private inner class MvpViewHandler(private val mvpView: V) : InvocationHandler {
        override fun invoke(proxy: Any, method: Method, args: Array<out Any>): Any? {
            return if (isViewAttached) {
                method.invoke(mvpView, args)
            } else null
        }

    }
}