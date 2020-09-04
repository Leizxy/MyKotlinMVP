package com.leizy.demo

import cn.leizy.lib.router.service.HelloService
import com.alibaba.android.arouter.facade.annotation.Autowired
import com.alibaba.android.arouter.launcher.ARouter

/**
 * @author Created by wulei
 * @date 2020/9/4, 004
 * @description
 */
class Test {
    @Autowired
    @JvmField
    var helloService: HelloService? = null

    init {
        ARouter.getInstance().inject(this)
    }

    fun test(): String {
        return helloService!!.sayHello("test")
    }
}