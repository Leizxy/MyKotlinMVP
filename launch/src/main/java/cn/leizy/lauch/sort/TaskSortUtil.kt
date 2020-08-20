package cn.leizy.lauch.sort

import android.annotation.SuppressLint
import android.util.ArraySet
import androidx.annotation.NonNull
import cn.leizy.lauch.DispatcherLog
import cn.leizy.lauch.task.Task
import java.util.*

/**
 * @author Created by wulei
 * @date 2020/8/7, 007
 * @description
 */
object TaskSortUtil {
    //高优先级的Task
    private val newTasksHigh: MutableList<Task> = ArrayList()

    @SuppressLint("NewApi")
    @Synchronized
    fun getSortResult(originTasks: MutableList<Task>, clsLaunchTasks: MutableList<Class<out Task?>>): MutableList<Task>? {
        var makeTime = System.currentTimeMillis()
        var dependSet: MutableSet<Int> = ArraySet()
        var graph = Graph(originTasks.size)
        for (i in originTasks.indices) {
            val task = originTasks[i]
            if (task.isSend || task.dependsOn() == null || task.dependsOn()!!.isEmpty()) {
                continue
            }
            for (cls in task.dependsOn()!!) {
                val indexOfDepend = getIndexOfTask(originTasks, clsLaunchTasks, cls)
                if (indexOfDepend < 0) {
                    throw IllegalStateException(task.javaClass.simpleName + " depends on " + cls!!.simpleName + " can not be found in task list.")
                }
                dependSet.add(indexOfDepend)
                graph.addEdge(indexOfDepend, i)
            }
        }
        val indexList: MutableList<Int> = graph.topologicalSort()
        val newTasksAll: MutableList<Task> = getResultTasks(originTasks, dependSet, indexList)
        DispatcherLog.i("task analyse cost makeTime " + (System.currentTimeMillis() - makeTime))
        printAllTaskName(newTasksAll)
        return newTasksAll
    }

    private fun printAllTaskName(newTasksAll: MutableList<Task>) {
        for (task in newTasksAll) {
            DispatcherLog.i(task.javaClass.simpleName)
        }
    }

    @NonNull
    private fun getResultTasks(originTasks: MutableList<Task>, dependSet: MutableSet<Int>, indexList: MutableList<Int>): MutableList<Task> {
        val newTasksAll: MutableList<Task> = ArrayList(originTasks.size)
        //被别人依赖的
        val newTasksDepended: MutableList<Task> = ArrayList()
        //没有依赖的
        val newTasksWithOutDepend: MutableList<Task> = ArrayList()
        //需要提升自己优先级的，先执行（这个先是相对于没有依赖的先）
        val newTasksRunAsSoon: MutableList<Task> = ArrayList()
        for (index in indexList) {
            if (dependSet.contains(index)) {
                newTasksDepended.add(originTasks[index])
            } else {
                val task: Task = originTasks[index]
                if (task.needRunAsSoon()) {
                    newTasksRunAsSoon.add(task)
                } else {
                    newTasksWithOutDepend.add(task)
                }
            }
        }
        // 顺序：被别人依赖的-->需要提升自己优先级的-->需要被等待的-->没有依赖的
        newTasksHigh.addAll(newTasksDepended)
        newTasksHigh.addAll(newTasksRunAsSoon)
        newTasksAll.addAll(newTasksHigh)
        newTasksAll.addAll(newTasksWithOutDepend)
        return newTasksAll
    }

    /**
     * 获取任务在任务列表中的index
     * @return
     */
    private fun getIndexOfTask(originTasks: MutableList<Task>, clsLaunchTasks: MutableList<Class<out Task?>>, cls: Class<out Task?>?): Int {
        val index = clsLaunchTasks.indexOf(cls)
        if (index >= 0) {
            return index
        }
        val size = originTasks.size
        for (i in 0..size) {
            if (cls!!.simpleName.equals(originTasks[i].javaClass.simpleName)) {
                return i
            }
        }
        return index
    }
}