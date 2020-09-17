package cn.leizy.lib.util

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.os.Looper
import android.text.TextUtils
import android.widget.Toast
import androidx.annotation.IdRes

/**
 * @author Created by wulei
 * @date 2019-11-13
 * @description
 */
class ToastUtil {
    init {
        throw UnsupportedOperationException("ToastUtil can not be Instantiate!")
    }

    companion object {
        private var isShow: Boolean = true
        private var toast: Toast? = null

        /**
         * 控制显示toast与否
         */
        fun controlShow(show: Boolean) {
            isShow = show
        }

        /**
         * 取消显示
         */
        fun cancelToast() {
            if (isShow) {
                toast!!.cancel()
            }
        }

        fun showToast(context: Context, string: String?) {
            if (!isShow && TextUtils.isEmpty(string)) {
                return
            }
            when (toast) {
                null -> toast =
                    Toast.makeText(context.applicationContext, string, Toast.LENGTH_SHORT)
                else -> toast!!.setText(string)
            }
            toast!!.show()
        }

        @SuppressLint("ResourceType")
        fun showToast(context: Context, @IdRes resId: Int) {
            showToast(context.applicationContext, context.resources.getString(resId))
        }

    }
}