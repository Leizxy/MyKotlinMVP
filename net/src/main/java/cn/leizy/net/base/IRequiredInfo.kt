package cn.leizy.net.base

import android.app.Application
import android.content.Context

/**
 * @author Created by wulei
 * @date 2020/9/22, 022
 * @description
 */
interface IRequiredInfo {
    fun isDebug(): Boolean

    fun getApplication(): Application
}