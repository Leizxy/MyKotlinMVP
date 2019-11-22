package cn.leizy.lib.data

/**
 * @author Created by wulei
 * @date 2019-11-11
 * @description
 */
class DataManager private constructor() {
    init {
        init()
    }

    private fun init() {

    }

    companion object {
        val instance = Holder.holder
    }

    private object Holder {
        val holder = DataManager()
    }
}