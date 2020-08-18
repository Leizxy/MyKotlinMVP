package com.scwlyd.tmslib.lauch.task

import android.os.Looper
import android.os.Process
import androidx.core.os.TraceCompat
import com.scwlyd.tmslib.lauch.DispatcherLog
import com.scwlyd.tmslib.lauch.TaskDispatcher
import com.scwlyd.tmslib.lauch.stat.TaskStat

/**
 * @author Created by wulei
 * @date 2020/8/17, 017
 * @description
 */
class DispatchRunnable(private var task: Task) : Runnable {
    private var taskDispatcher: TaskDispatcher? = null

    constructor(task: Task, dispatcher: TaskDispatcher) : this(task) {
        this.taskDispatcher = dispatcher
    }

    override fun run() {
        TraceCompat.beginSection(task.javaClass.simpleName)
        DispatcherLog.i(task.javaClass.simpleName + " begin run Situation " + TaskStat.currentSituation)
        Process.setThreadPriority(task.priority())

        var startTime = System.currentTimeMillis()
        task.isWaiting = true
        task.waitToSatisfy()
        var waitTime = System.currentTimeMillis()
        startTime = System.currentTimeMillis()

        //执行task任务
        task.isRunning = true
        task.run()
        //执行task的尾部任务
        task.getTailRunnable()?.run()

        if (!task.needCall() || !task.runOnMainThread()) {
            printTaskLog(startTime, waitTime)
            TaskStat.markTaskDone()
            task.isFinished = true
            taskDispatcher!!.satisfyChildren(task)
            taskDispatcher!!.markTaskDone(task)
            DispatcherLog.i(task.javaClass.simpleName + " finish.")
        }
        TraceCompat.endSection()
    }

    private fun printTaskLog(startTime: Long, waitTime: Long) {
        val runTime = System.currentTimeMillis() - startTime
        if (DispatcherLog.isDebug) {
            DispatcherLog.i(task.javaClass.simpleName + " wait " + waitTime + "  run  "
                    + runTime + " isMain " + (Looper.getMainLooper() == Looper.myLooper())
                    + "  needWait " + (task.needWait() || (Looper.getMainLooper() == Looper.myLooper()))
                    + "  ThreadId " + Thread.currentThread().id
                    + "  ThreadName " + Thread.currentThread().name
                    + "  Situation " + TaskStat.currentSituation
            )
        }
    }
}