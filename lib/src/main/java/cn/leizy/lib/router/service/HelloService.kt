package cn.leizy.lib.router.service

import android.content.Context
import com.alibaba.android.arouter.facade.template.IProvider

/**
 * @author Created by wulei
 * @date 2020/9/4, 004
 * @description
 */
interface HelloService : IProvider {
    fun sayHello(string: String):String
}