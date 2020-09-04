package cn.leizy.lib.router.service

import android.content.Context
import com.alibaba.android.arouter.facade.annotation.Route

/**
 * @author Created by wulei
 * @date 2020/9/4, 004
 * @description
 */
@Route(path = "/service/hello", name = "测试服务")
class HelloServiceImpl : HelloService {
    override fun init(context: Context?) {

    }

    override fun sayHello(string: String): String {
        return "Say $string"
    }
}