package cn.leizy.lib.http.okgo

import com.google.gson.FieldNamingPolicy
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.internal.bind.DateTypeAdapter
import java.util.*

/**
 * @author Created by wulei
 * @date 2019-11-11
 * @description
 */
class JsonConvertor private constructor() {
    companion object {
        private var gson: Gson? = null
        fun getInstance(): Gson {
            if (gson == null) {
                gson = GsonBuilder()
                    .setPrettyPrinting()
                    .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                    .registerTypeAdapter(Date::class.java, DateTypeAdapter())
                    .create()
            }
            return gson!!
        }
    }
}