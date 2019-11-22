package cn.leizy.lib.base

/**
 * @author Created by wulei
 * @date 2019-11-16
 * @description
 */
interface IView {
    /**
     * @param key 对应请求
     * @param obj 对应成功后返回的对象
     */
//    fun onSuccess(key: Int, obj: Any)
    /**
     * @param msg 请求错误的返回结果
     */
    fun onError(msg: String)
}