package cn.leizy.lib.router.service

import android.content.Context
import com.alibaba.android.arouter.facade.Postcard
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.facade.service.DegradeService

/**
 * @author Created by wulei
 * @date 2020/9/4, 004
 * @description
 */
@Route(path = "/lib/degrade")
class DegradeServiceImpl : DegradeService {
    override fun init(context: Context?) {

    }

    /**
     * 当调用navigation()方法没有传入@NavigationCallback时，会调用该方法，
     * 用于处理全局未找到跳转目标的策略。
     */
    override fun onLost(context: Context?, postcard: Postcard?) {
    }
}