package cn.leizy.lib.http.okgo

import com.lzy.okgo.callback.AbsCallback
import okhttp3.Response
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type

/**
 * @author Created by wulei
 * @date 2019-11-11
 * @description
 */
abstract class MyCallback<T>() : AbsCallback<T>() {
    private var type: Type? = null
    private var clazz: Class<T>? = null

    init {

    }

    constructor(type: Type) : this() {
        this.type = type
    }

    constructor(clazz: Class<T>) : this() {
        this.clazz = clazz
    }

    override fun convertResponse(response: Response): T {
        if (type == null) {
            if (clazz == null) {
                val genType = javaClass.genericSuperclass
                type = (genType as ParameterizedType).actualTypeArguments[0]
            } else {
                val convert = JsonConvert<T>(clazz!!)
                return convert.convertResponse(response)!!
            }
        }
        val convert = JsonConvert<T>(type!!)
        return convert.convertResponse(response)!!
    }
}