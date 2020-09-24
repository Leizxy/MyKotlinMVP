package cn.leizy.lib.base

/**
 * @author Created by wulei
 * @date 2019-11-16
 * @description
 */
interface JPresenter<V, M> {
    fun attachView(view: V)
    fun detachView()
}