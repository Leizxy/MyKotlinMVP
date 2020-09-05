package cn.leizy.lib.router.service

import android.content.Context
import android.net.Uri
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.facade.service.PathReplaceService

/**
 * @author Created by wulei
 * @date 2020/9/4, 004
 * @description
 */
@Route(path = "/lib/pathReplace")
class PathReplaceServiceImpl : PathReplaceService {
    override fun init(context: Context?) {

    }

    override fun forString(path: String?): String {
        return path!!
    }

    override fun forUri(uri: Uri?): Uri {
        return uri!!
    }
}