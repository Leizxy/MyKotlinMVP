package cn.leizy.lib.base.mvp

/**
 * @author Created by wulei
 * @date 2019-11-16
 * @description
 */
interface IView {
    /**
     * 显示 loading
     */
    fun showLoading(){}

    /**
     * 隐藏 loading
     */
    fun hideLoading(){}

    fun showToast(str: String?)

    fun showToast(stringId: Int)
    /**
     * @param key 请求错误的返回结果
     * @see cn.leizy.lib.http.HttpKeys
     * @return 返回请求的参数
     */
    fun getParams(key: Int): Map<String, Any>?{return null}

    /**
     * @param key
     * @see getParams 根据key来处理成功
     * @param obj 请求返回后处理返回结果为对应Object
     */
    fun success(key: Int, obj: Any){}

    /**
     * @see success
     */
    fun fail(key: Int, obj: Any){}
}