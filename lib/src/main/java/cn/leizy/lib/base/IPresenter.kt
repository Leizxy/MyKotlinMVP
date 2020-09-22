package cn.leizy.lib.base

/**
 * @author Created by wulei
 * @date 2019-11-16
 * @description
 */
interface IPresenter<V : IView, M : IModel> {
    fun <V : IView> attachView(view: V)
    fun detachView()
}