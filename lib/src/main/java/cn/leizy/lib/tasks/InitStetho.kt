package cn.leizy.lib.tasks

import android.app.Application
import cn.leizy.lauch.task.Task
import cn.leizy.lib.BuildConfig
import com.facebook.stetho.Stetho

/**
 * @author Created by wulei
 * @date 2020/9/29, 029
 * @description
 */
class InitStetho : Task() {
    override fun run() {
        if (BuildConfig.DEBUG) {
            Stetho.initializeWithDefaults(context as Application)
        }
    }
}