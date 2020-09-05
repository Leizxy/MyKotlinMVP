package cn.leizy.lauch.task

/**
 * @author Created by wulei
 * @date 2020/8/18, 018
 * @description
 */
abstract class MainTask : Task() {
    override fun runOnMainThread(): Boolean {
        return true
    }
}