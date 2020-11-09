package cn.leizy.lib.http

import android.content.Intent
import android.util.Log
import cn.leizy.net.BuildConfig
import cn.leizy.net.R
import com.alibaba.android.arouter.launcher.ARouter
import retrofit2.HttpException
import java.net.ConnectException
import java.net.SocketTimeoutException

/**
 * @author Created by wulei
 * @date 2020/10/27, 027
 * @description
 */
object Exceptions {
    private const val UNAUTHORIZED = 401
    fun handleException(e: Throwable) {
        Log.e("Exceptions", "handleException: ", e)
        when (e) {
            is SocketTimeoutException -> {
                if (!BuildConfig.DEBUG) {
                } else {
                }
            }
            is ConnectException -> {
                if (!BuildConfig.DEBUG) {
                } else {
                }
            }
            is HttpException -> {
                when (e.code()) {
                    UNAUTHORIZED -> {
/*                        ARouter.getInstance().build("/login/login")
                                .withFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                                .navigation()*/
                    }
                }
            }
            else -> {}
        }
    }
}