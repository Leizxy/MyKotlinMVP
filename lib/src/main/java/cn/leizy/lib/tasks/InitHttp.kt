package cn.leizy.lib.tasks

import android.app.Application
import cn.leizy.lauch.task.Task
import cn.leizy.lib.http.HttpProxy

/**
 * @author Created by wulei
 * @date 2020/8/18
 * @description
 */
class InitHttp : Task() {

    override fun run() {
        HttpProxy.init(context as Application)
    }
}