package cn.leizy.lib.util

import android.app.Activity
import android.content.Context
import android.os.Looper
import android.text.TextUtils
import android.widget.Toast

/**
 * @author Created by wulei
 * @date 2019-11-13
 * @description
 */
class ToastUtil {
    companion object {
        fun showToast(context: Context, string: String) {
            if (!TextUtils.isEmpty(string)) {
                Toast.makeText(context, string, Toast.LENGTH_SHORT).show()
            }
        }

        fun showToast(context: Context, resId: Int) {
            Toast.makeText(context, context.resources.getText(resId), Toast.LENGTH_SHORT).show()
        }

        fun showToastInMainThread(context: Activity, string: String) {
            context.runOnUiThread { showToast(context, string) }
        }

        fun showToastInMainThread(context: Activity, resId: Int) {
            context.runOnUiThread { showToast(context, resId) }
        }

        fun showToastWithLooper(context: Context, string: String) {
            Looper.prepare()
            showToast(context, string)
            Looper.loop()
        }
    }
}