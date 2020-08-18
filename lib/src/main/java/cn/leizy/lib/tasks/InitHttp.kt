package cn.leizy.lib.tasks

import android.app.Application
import cn.leizy.lib.http.HttpProxy
import com.scwlyd.tmslib.lauch.task.Task

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