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

    /**
     * @param key 请求错误的返回结果
     * @see cn.leizy.lib.http.HttpKeys
     * @return 返回请求的参数
     */
    fun getParams(key: Int): Map<String, Any>

    /**
     * @param key
     * @see getParams 根据key来处理成功
     * @param obj 请求返回后处理返回结果为对应Object
     */
    fun success(key: Int, obj: Any)

    /**
     * @see success
     */
    fun fail(key: Int, obj: Any)
}