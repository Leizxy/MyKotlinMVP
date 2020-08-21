package cn.leizy.lib.tasks

import android.app.Application
import cn.leizy.lauch.task.MainTask
import cn.leizy.lauch.task.Task
import cn.leizy.lib.BuildConfig
import com.alibaba.android.arouter.launcher.ARouter

/**
 * @author Created by wulei
 * @date 2020/8/21, 021
 * @description
 */
class InitARouter : MainTask() {
    override fun run() {
        if (BuildConfig.DEBUG) {
            ARouter.openLog()
            ARouter.openDebug()
        }
        ARouter.init(context as Application)
    }
}