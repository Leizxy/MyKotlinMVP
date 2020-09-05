package cn.leizy.lib.http

import cn.leizy.lib.App

/**
 * @author Created by wulei
 * @date 2020/8/27, 027
 * @description
 */
abstract class  BaseCallback<T> {
    abstract fun onSuccess(body : T)

    fun onFail(t: Throwable) {
//        MyLiveDataBus.instance.post(App.getInstance().getCurrentStr() + IHttp.HTTP_TOAST, t.message)
        App.getInstance().toast(t.message)
    }
}