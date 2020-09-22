package cn.leizy.lib.util

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.os.Looper
import android.text.TextUtils
import android.widget.Toast
import androidx.annotation.IdRes
import cn.leizy.lib.App

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
        private var context: Context? = null

        fun init(context: Context) {
            this.context = context
        }

        fun showToast(context: Context = App.getInstance(), string: String?) {
            if (!isShow && TextUtils.isEmpty(string)) {
                return
            }
            Looper.prepare()
            when (toast) {
                null -> toast =
                    Toast.makeText(context, string, Toast.LENGTH_SHORT)
                else -> toast!!.setText(string)
            }
            toast!!.show()
            Looper.loop()
        }

        @SuppressLint("ResourceType")
        fun showToast(context: Context = App.getInstance(), @IdRes resId: Int) {
            showToast(context, context.resources.getString(resId))
        }

    }
}