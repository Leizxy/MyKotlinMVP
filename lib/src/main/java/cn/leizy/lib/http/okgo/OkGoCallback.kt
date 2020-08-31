package cn.leizy.lib.http.okgo

import cn.leizy.lib.http.BaseCallback

/**
 * @author Created by wulei
 * @date 2019-11-11
 * @description
 */
abstract class OkGoCallback<T> : BaseCallback<T>() {
    fun onStart() {}

    fun onFinish() {}
}