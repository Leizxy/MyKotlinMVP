package cn.leizy.lib.data

/**
 * @author Created by wulei
 * @date 2019-11-11
 * @description
 */
interface IConfig {
    companion object {
        val USER_PHONE: String
            get() = "user_phone"
        val TOKEN_KEY: String
            get() = "user_token"
    }

    fun savePhone(phone: String)

    fun getPhone(): String

}