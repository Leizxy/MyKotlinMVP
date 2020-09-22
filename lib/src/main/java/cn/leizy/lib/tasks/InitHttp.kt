package cn.leizy.lib.tasks

import android.app.Application
import cn.leizy.lauch.task.Task
import cn.leizy.lib.http.HttpProxy
import cn.leizy.lib.retrofit.CommonApi
import cn.leizy.lib.retrofit.HttpInterface
import cn.leizy.lib.retrofit.RetrofitUtil
import cn.leizy.net.base.IRequiredInfo
import cn.leizy.net.base.NetWorkApi

/**
 * @author Created by wulei
 * @date 2020/8/18
 * @description
 */
class InitHttp : Task() {

    override fun run() {
        HttpProxy.init(context as Application)
        NetWorkApi.init(context as IRequiredInfo)
    }
}