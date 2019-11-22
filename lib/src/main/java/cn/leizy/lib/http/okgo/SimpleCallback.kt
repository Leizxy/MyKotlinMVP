package cn.leizy.lib.http.okgo

/**
 * @author Created by wulei
 * @date 2019-11-11
 * @description
 */
abstract class SimpleCallback<T> {
    abstract fun onStart()
    abstract fun onSuccess(t:T)
    abstract fun onFail(e:Throwable)
    abstract fun onFinish()
}