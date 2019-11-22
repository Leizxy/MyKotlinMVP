package cn.leizy.lib.base

import java.lang.ref.WeakReference
import java.lang.reflect.InvocationHandler
import java.lang.reflect.Method
import java.lang.reflect.Proxy

/**
 * @author Created by wulei
 * @date 2019-11-16
 * @description
 */
abstract class BasePresenter<V : IView, M : IModel> : IPresenter<V, M> {
    protected var mView: V? = null
    protected var model: M? = null
    private var weakReference: WeakReference<V>? = null

    protected val isViewAttached: Boolean
        get() = weakReference != null && weakReference?.get() != null

    override fun attachView(view: V) {
        weakReference = WeakReference(view)
        mView = Proxy.newProxyInstance(
            view.javaClass.classLoader,
            view.javaClass.interfaces,
            MvpViewHandler(weakReference?.get()!!)
        ) as V
        if (model == null) {
            model = createModel()
        }
    }

    abstract fun createModel(): M

    override fun detachView() {
        model!!.cancelHttp()
        model = null
        if (isViewAttached) {
            weakReference!!.clear()
            weakReference = null
        }
    }


    private inner class MvpViewHandler internal constructor(private val mvpView: IView) : InvocationHandler {
        @Throws(Throwable::class)
        override fun invoke(proxy: Any, method: Method, args: Array<out Any>): Any? {
            //如果V层没被销毁, 执行V层的方法.
            return if (isViewAttached) {
                method.invoke(mvpView, *args)
            } else null //P层不需要关注V层的返回值
        }
    }

}