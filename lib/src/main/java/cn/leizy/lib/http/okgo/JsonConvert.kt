package cn.leizy.lib.http.okgo

import cn.leizy.lib.App
import cn.leizy.lib.http.IHttp
import com.google.gson.stream.JsonReader
import com.lzy.okgo.convert.Converter
import cn.leizy.lib.http.bean.HttpResponse
import cn.leizy.lib.util.MyLiveDataBus
import okhttp3.Response
import org.json.JSONArray
import org.json.JSONObject
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type

/**
 * @author Created by wulei
 * @date 2019-11-11
 * @description
 */
class JsonConvert<T>() : Converter<T> {
    private var type: Type? = null
    private var clazz: Class<T>? = null

    constructor(type: Type) : this() {
        this.type = type
    }

    constructor(clazz: Class<T>) : this() {
        this.clazz = clazz
    }

    override fun convertResponse(response: Response): T? {
        if (type == null) {
            if (clazz == null) {
                var genType = javaClass.genericSuperclass
                type = (genType as ParameterizedType).actualTypeArguments[0]
            } else {
                return parseClass(response, clazz)
            }
        }
        if (type is ParameterizedType) {
            return parseParameterizedType(response, type as ParameterizedType)
        } else if (type is Class<*>) {
            return parseClass(response, type as Class<T>)
        } else {
            return parseType(response, type)
        }
    }

    private fun parseType(response: Response, type: Type?): T? {
        if (type == null) return null
        val body = response.body()
        if (body == null) return null
        val jsonReader = JsonReader(body.charStream())
        val t: T = Convert.fromJson<T>(jsonReader, type)
        response.close()
        return t
    }

    private fun parseClass(response: Response, rawType: Class<T>?): T? {
        if (rawType == null)
            return null
        val body = response.body() ?: return null
        var jsonReader = JsonReader(body.charStream())
        if (rawType == String::class.java) {
            return body.string() as T
        } else if (rawType == JSONObject::class.java) {
            return JSONObject(body.string()) as T
        } else if (rawType == JSONArray::class.java) {
            return JSONArray(body.string()) as T
        } else {
            val httpResponse = Convert.fromJson<HttpResponse<*>>(jsonReader, rawType)
            response.close()
            if (!httpResponse.isSuccess) {
                MyLiveDataBus.instance.post(
                    App.getInstance().getCurrentStr() + IHttp.HTTP_TOAST,
                    httpResponse.message
                )
            }
            return httpResponse as T
        }
    }

    private fun parseParameterizedType(response: Response, type: ParameterizedType): T? {
        if (type == null) return null
        val body = response.body()
        if (body == null) return null
        val jsonReader = JsonReader(body.charStream())

        //泛型实际类型
        val rawType = type.rawType
        //泛型参数
        val typeArgument = type.actualTypeArguments[0]
        if (rawType != HttpResponse::class.java) {
            val t: T = Convert.fromJson(jsonReader, type)
            response.close()
            return t
        } else {
            //TODO 按项目需要重写
            when (response.code()) {
                200 -> {
                    val httpResponse =
                        Convert.fromJson<HttpResponse<*>>(jsonReader, type)
                    response.close()
                    if (!httpResponse.isSuccess) {
                        MyLiveDataBus.instance.post(
                            App.getInstance().getCurrentStr() + IHttp.HTTP_TOAST,
                            httpResponse.message
                        )
                    }
                    return httpResponse as T
                }
                else -> return null
            }

        }
    }
}