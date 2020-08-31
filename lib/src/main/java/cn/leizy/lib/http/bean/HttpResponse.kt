package cn.leizy.lib.http.bean

/**
 * @author Created by wulei
 * @date 2019-11-11
 * @description
 */
class HttpResponse<T> {
    var IsSuccess: Boolean = false
//    var message: String? = null
    var OperationDesc: String? = null
//    var errorCode: Int? = null
    var ResultCode: Int? = null
//    var data: T? = null
    var Result: T? = null
}