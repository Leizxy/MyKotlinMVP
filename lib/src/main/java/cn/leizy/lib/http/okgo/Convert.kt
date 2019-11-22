package cn.leizy.lib.http.okgo

import com.google.gson.Gson
import com.google.gson.JsonIOException
import com.google.gson.JsonParser
import com.google.gson.JsonSyntaxException
import com.google.gson.stream.JsonReader
import java.io.Reader
import java.lang.Exception
import java.lang.reflect.Type

/**
 * @author Created by wulei
 * @date 2019-11-11
 * @description
 */
object Convert {
    private fun create(): Gson {
        return Convert.GsonHolder.gson
    }

    private object GsonHolder {
        val gson = Gson()
    }

    @Throws(JsonIOException::class, JsonSyntaxException::class)
    fun <T> fromJson(json: String, type: Class<T>): T {
        return create().fromJson(json, type)
    }

    fun <T> fromJson(json: String, type: Type): T {
        return create().fromJson(json, type)
    }

    @Throws(JsonIOException::class, JsonSyntaxException::class)
    fun <T> fromJson(reader: JsonReader, typeOfT: Type): T {
        return create().fromJson(reader, typeOfT)
    }


    @Throws(JsonSyntaxException::class, JsonIOException::class)
    fun <T> fromJson(json: Reader, classOfT: Class<T>): T {
        return create().fromJson(json, classOfT)
    }

    @Throws(JsonIOException::class, JsonSyntaxException::class)
    fun <T> fromJson(json: Reader, typeOfT: Type): T {
        return create().fromJson(json, typeOfT)
    }

    fun toJson(src: Any): String {
        return create().toJson(src)
    }

    fun toJson(src: Any, type: Type): String {
        return create().toJson(src, type)
    }

    fun formatJson(json: String): String {
        return try {
            val jp = JsonParser()
            val je = jp.parse(json)
            JsonConvertor.getInstance().toJson(je)
        } catch (e: Exception) {
            json
        }
    }

    fun formatJson(src: Any): String {
        return try {
            val jp = JsonParser()
            val je = jp.parse(toJson(src))
            JsonConvertor.getInstance().toJson(je)
        } catch (e: Exception) {
            e.message!!
        }
    }
}