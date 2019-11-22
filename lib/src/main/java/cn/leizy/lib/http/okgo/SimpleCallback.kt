package cn.leizy.lib.http.okgo

import cn.leizy.lib.App
import cn.leizy.lib.http.IHttp
import cn.leizy.lib.util.MyLiveDataBus

/**
 * @author Created by wulei
 * @date 2019-11-11
 * @description
 */
abstract class SimpleCallback<T> {
    fun onStart() {}

    abstract fun onSuccess(t: T)

    fun onFail(t: Throwable) {
        MyLiveDataBus.instance.post(App.getInstance().getCurrentStr() + IHttp.HTTP_TOAST, t.message)
    }

    fun onFinish() {}
}