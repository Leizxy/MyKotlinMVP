package cn.leizy.lib.model

/**
 * @author Created by wulei
 * @date 2019-11-11
 * @description
 */
class HttpResponse<T> {
    var isSuccess: Boolean = false
    var message: String? = null
    var errorCode: Int? = null
    var data: T? = null
}