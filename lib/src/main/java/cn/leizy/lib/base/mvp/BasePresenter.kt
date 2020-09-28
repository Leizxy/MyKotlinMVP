package cn.leizy.lib.base.mvp

/**
 * @author Created by wulei
 * @date 2020/9/24, 024
 * @description
 */
abstract class BasePresenter<V, M> {
    protected var view: V? = null
    protected var model: M? = null
    protected var isViewAttached = this.view == null
    fun attachView(view: V) {
        this.view = view
        if (this.model == null) {
            this.model = createModel()
        }
    }

    abstract fun createModel(): M?

    open fun dettachView() {
        this.view = null
    }
}