package cn.leizy.lauch

import android.content.Context
import android.os.Looper
import androidx.annotation.UiThread
import cn.leizy.lauch.sort.TaskSortUtil
import cn.leizy.lauch.stat.TaskStat
import cn.leizy.lauch.task.DispatchRunnable
import cn.leizy.lauch.task.Task
import cn.leizy.lauch.task.TaskCallback
import java.util.concurrent.CountDownLatch
import java.util.concurrent.Future
import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicInteger

/**
 * @author Created by wulei
 * @date 2020/8/4, 004
 * @description 启动器
 */
class TaskDispatcher private constructor() {
    private var startTime: Long = 0
    private val futures: MutableList<Future<Any>> = ArrayList()

    private var allTasks: MutableList<Task> = ArrayList()
    private val clsAllTasks: MutableList<Class<out Task?>> = ArrayList()

    @Volatile
    private var mainThreadTasks: MutableList<Task> = ArrayList()

    private var countDownLatch: CountDownLatch? = null

    //保存需要Wait的Task的数量
    private val needWaitCount = AtomicInteger()

    //调用了await的时候还没结束的且需要等待的Task
    private val needWaitTasks: MutableList<Task> = ArrayList()

    //已经结束了的Task
    @Volatile
    private var finishedTasks: MutableList<Class<out Task?>> = ArrayList(100)
    private val dependedHashMap: HashMap<Class<out Task?>, ArrayList<Task>?> = HashMap()

    //启动器分析的次数，统计下分析的耗时；
    private val analyseCount = AtomicInteger()

    fun addTask(task: Task?): TaskDispatcher {
        if (task != null) {
            collectDepends(task)
            allTasks.add(task)
            clsAllTasks.add(task.javaClass)
            if (ifNeedWait(task)) {
                needWaitTasks.add(task)
                needWaitCount.getAndIncrement()
            }
        }
        return this
    }

    private fun collectDepends(task: Task) {
        if (task.dependsOn() != null && task.dependsOn()!!.size > 0) {
            for (cls in task.dependsOn()!!) {
                if (dependedHashMap[cls!!] == null) {
                    dependedHashMap[cls] = ArrayList()
                }
                dependedHashMap[cls]!!.add(task)
                if (finishedTasks.contains(cls)) {
                    task.satisfy()
                }
            }
        }
    }

    private fun ifNeedWait(task: Task): Boolean {
        return !task.runOnMainThread() && task.needWait()
    }

    @UiThread
    fun start() {
        startTime = System.currentTimeMillis()
        if (Looper.getMainLooper() != Looper.myLooper()) {
            throw RuntimeException("you must call start() from UiThread")
        }
        if (allTasks.size > 0) {
            analyseCount.getAndIncrement()
            printDependedMsg()
            allTasks = TaskSortUtil.getSortResult(allTasks, clsAllTasks)!!
            countDownLatch = CountDownLatch(needWaitCount.get())
            sendAndExecuteAsyncTasks()
            DispatcherLog.i("task analyse cost " + (System.currentTimeMillis() - startTime) + " begin main.")
            executeTaskMain()
        }
        DispatcherLog.i("task analyse cost startTime cost " + (System.currentTimeMillis() - startTime))
    }

    fun cancel() {
        for (future in futures) {
            future.cancel(true)
        }
    }

    private fun executeTaskMain() {
        startTime = System.currentTimeMillis()
        for (task in mainThreadTasks) {
            val time = System.currentTimeMillis()
            DispatchRunnable(task, this).run()
            DispatcherLog.i("real main " + task.javaClass.simpleName + " cost " + (System.currentTimeMillis() - time))
        }
        DispatcherLog.i("main task cost " + (System.currentTimeMillis() - startTime))
    }

    private fun sendAndExecuteAsyncTasks() {
        for (task in allTasks) {
            if (task.onlyInMainProcess() && !isMainProcess) {
                markTaskDone(task)
            } else {
                sendTaskReal(task)
            }
            task.isSend = true
        }
    }

    private fun sendTaskReal(task: Task) {
        if (task.runOnMainThread()) {
            mainThreadTasks.add(task)
            if (task.needCall()) {
                task.setTaskCallBack(object : TaskCallback {
                    override fun call() {
                        TaskStat.markTaskDone()
                        task.isFinished = true
                        satisfyChildren(task)
                        markTaskDone(task)
                        DispatcherLog.i(task.javaClass.simpleName + " finish")
                    }
                })
            }
        } else {
            //直接发，是否执行取决于具体线程池
            val future: Future<*>? = task.runOn().submit(DispatchRunnable(task, this))
            futures.add(future as Future<Any>)
        }
    }

    /**
     * 通知Children一个前置任务已完成
     *
     * @param launchTask
     */
    fun satisfyChildren(launchTask: Task) {
        val arrayList: ArrayList<Task>? = dependedHashMap.get(launchTask.javaClass)
        if (arrayList != null && arrayList.size > 0) {
            for (task in arrayList) {
                task.satisfy()
            }
        }
    }

    fun markTaskDone(task: Task) {
        if (ifNeedWait(task)) {
            finishedTasks.add(task.javaClass)
            needWaitTasks.remove(task)
            countDownLatch!!.countDown()
            needWaitCount.getAndIncrement()
        }
    }

    private fun printDependedMsg() {
        DispatcherLog.i("needWait size : " + needWaitCount.get())
        for (cls in dependedHashMap.keys) {
            DispatcherLog.i("cls " + cls.simpleName + " " + dependedHashMap[cls]!!.size)
            for (task in dependedHashMap[cls]!!) {
                DispatcherLog.i("cls " + task.javaClass.simpleName)
            }
        }
    }

    fun executeTask(task: Task) {
        if (ifNeedWait(task)) {
            needWaitCount.getAndIncrement()
        }
        task.runOn().execute(DispatchRunnable(task, this))
    }

    @UiThread
    fun await() {
        try {
            if (DispatcherLog.isDebug) {
                DispatcherLog.i("still has " + needWaitCount.get())
                for (task in needWaitTasks) {
                    DispatcherLog.i("needWait: " + task.javaClass.simpleName)
                }
            }
            if (needWaitCount.get() > 0) {
                countDownLatch!!.await(WAIT_TIME, TimeUnit.MILLISECONDS)
            }
        } catch (e: InterruptedException) {

        }
    }

    private fun isAllTaskDone(): Boolean {
        var done = true
        for (task in allTasks) {
            done = done and task.isFinished
        }
        DispatcherLog.i("allTaskDone $done")
        return done
    }

    companion object {
        private const val WAIT_TIME: Long = 10 * 1000
        private var isMainProcess: Boolean = false
        private var hasInit: Boolean = false
        private var context: Context? = null
        private var dispatcher: TaskDispatcher? = null

        fun init(context: Context) {
            this.context = context
            this.hasInit = true
            this.isMainProcess = Util.isMainProcess(context)
        }

        fun createInstance(): TaskDispatcher {
            if (!hasInit) {
                throw RuntimeException("pls call ${TaskDispatcher::class.java.simpleName}.init first")
            }
            dispatcher = TaskDispatcher()
            return dispatcher!!
        }

        fun getContext(): Context {
            return context!!
        }

        fun isMainProcess(): Boolean {
            return isMainProcess
        }

        fun isAllDone(): Boolean {
            return dispatcher!!.isAllTaskDone()
        }

    }
}