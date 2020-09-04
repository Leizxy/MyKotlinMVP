package cn.leizy.lib.router.service

import android.content.Context
import android.util.Log
import com.alibaba.android.arouter.facade.Postcard
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.facade.service.PretreatmentService

/**
 * @author Created by wulei
 * @date 2020/9/4, 004
 * @description
 */
@Route(path = "/lib/pre")
class PretreatmentServiceImpl : PretreatmentService {
    override fun init(context: Context?) {

    }

    override fun onPretreatment(context: Context?, postcard: Postcard?): Boolean {
        Log.i("PretreatmentServiceImpl", "onPretreatment: ${postcard!!.path}")
        return true
    }
}