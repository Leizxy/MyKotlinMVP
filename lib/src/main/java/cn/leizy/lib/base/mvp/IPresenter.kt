package cn.leizy.lib.base.mvp

/**
 * @author Created by wulei
 * @date 2020/9/29, 029
 * @description
 */
interface IPresenter<V, M> {
    fun attachView(view: V)
    fun detachView()
}