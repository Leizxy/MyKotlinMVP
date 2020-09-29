package cn.leizy.lib.base

import cn.leizy.lib.base.mvp.IModel
import cn.leizy.lib.base.mvp.IView

/**
 * @author Created by wulei
 * @date 2019-11-16
 * @description kotlin没有用此接口。
 */
interface IPresenter<V, M> {
    fun attachView(view: V)
    fun detachView()
}