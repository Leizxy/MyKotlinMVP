package cn.leizy.lauch

import android.annotation.SuppressLint
import android.app.ActivityManager
import android.content.Context
import android.text.TextUtils
import java.io.BufferedReader
import java.io.FileInputStream
import java.io.InputStreamReader
import java.lang.Exception
import java.lang.StringBuilder

/**
 * @author Created by wulei
 * @date 2020/8/4, 004
 * @description
 */
object Util {
    var curProcessName: String? = null
    fun isMainProcess(context: Context): Boolean {
        val processName = getCurProcessName(context)
        if (processName!!.contains(":"))
            return false
        return processName == context.packageName
    }

    @SuppressLint("ServiceCast")
    private fun getCurProcessName(context: Context): String? {
        if (!TextUtils.isEmpty(curProcessName)) {
            return curProcessName
        }
        try {
            val myPid: Int = android.os.Process.myPid()
            val activityManager: ActivityManager = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
            for (appProcess in activityManager.runningAppProcesses) {
                if (appProcess.pid == myPid) {
                    curProcessName = appProcess.processName
                    return curProcessName
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        curProcessName = curProcessNameFromProc
        return curProcessName
    }

    private val curProcessNameFromProc: String?
        get() {
            var cmdLineReader: BufferedReader? = null
            try {
                cmdLineReader = BufferedReader(InputStreamReader(FileInputStream("/proc/" + android.os.Process.myPid() + "/cmdline"), "iso-8859-1"))
                val processName = StringBuilder()
                var c: Int
                while (cmdLineReader.read().also { c = it } > 0) {
                    processName.append(c.toChar())
                }
                return processName.toString()
            } catch (e: Throwable) {

            } finally {
                if (cmdLineReader != null) {
                    try {
                        cmdLineReader.close()
                    } catch (e: Exception) {

                    }
                }
            }
            return null
        }
}