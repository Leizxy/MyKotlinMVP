package cn.leizy.lib.tasks

import android.app.Application
import cn.leizy.lauch.task.Task
import cn.leizy.lib.BuildConfig
import com.squareup.leakcanary.LeakCanary

/**
 * @author Created by wulei
 * @date 2020/8/18, 018
 * @description
 */
class InitLeak : Task() {
    override fun run() {
        if (BuildConfig.DEBUG) {
            if (LeakCanary.isInAnalyzerProcess(context)) {
                //此过程专用于LeakCanary进行堆分析。在此过程中不应初始化应用程序。
                return
            }
            LeakCanary.install(context as Application)
        }
    }
}