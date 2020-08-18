package com.scwlyd.tmslib.lauch.stat

import com.scwlyd.tmslib.lauch.DispatcherLog
import java.util.concurrent.atomic.AtomicInteger

/**
 * @author Created by wulei
 * @date 2020/8/17, 017
 * @description
 */
object TaskStat {
    @Volatile
    var currentSituation: String = ""
        set(value) {
            if (!openLaunchStat) {
                return
            }
            DispatcherLog.i("currentSituation is {$value}")
            field = value
            setLaunchStat()
        }
    private var beans: MutableList<TaskStatBean> = ArrayList()
    private var taskDoneCount: AtomicInteger = AtomicInteger()

    //是否开启统计
    private var openLaunchStat: Boolean = false

    fun markTaskDone() {
        taskDoneCount.getAndIncrement()
    }

    fun setLaunchStat() {
        val bean = TaskStatBean()
        bean.situation = currentSituation
        bean.count = taskDoneCount.get()
        beans.add(bean)
        taskDoneCount = AtomicInteger(0)
    }

}