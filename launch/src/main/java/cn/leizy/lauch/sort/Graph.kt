package cn.leizy.lauch.sort

import java.util.*

/**
 * @author Created by wulei
 * @date 2020/8/7, 007
 * @description 有向无环图的拓扑排序算法
 */
class Graph constructor(private val verticeCount: Int) {

    //领接表
    private val mAdj: Array<MutableList<Int>?>

    init {
        mAdj = arrayOfNulls(verticeCount)
        for (i in 0 until verticeCount) {
            mAdj[i] = ArrayList()
        }
    }

    /**
     * 添加边
     */
    fun addEdge(from: Int, to: Int) {
        mAdj[from]!!.add(to)
    }

    fun topologicalSort(): Vector<Int> {
        val indegree = IntArray(verticeCount)
        //初始化所有点的入度数量
        for (i in 0 until verticeCount) {
            val temp: ArrayList<Int> = mAdj[i] as ArrayList<Int>
            for (node in temp) {
                indegree[node]++
            }
        }
        val queue: Queue<Int> = LinkedList()
        //找出所有入度为0的点
        for (i in 0 until verticeCount) {
            if (indegree[i] == 0) {
                queue.add(i)
            }
        }

        var cnt = 0
        val topOrder: Vector<Int> = Vector()
        while (!queue.isEmpty()) {
            val u: Int? = queue.poll()
            topOrder.add(u)
            //找到该点（入度为0）的所有邻接点
            for (node in mAdj[u!!]!!) {
                //把这个点的入度减1，如果入度变成了0，那么添加到入度0的队列里
                if (--indegree[node] == 0) {
                    queue.add(node)
                }
            }
            cnt++
        }
        if (cnt != verticeCount) {
            throw IllegalStateException("Exists a cycle in the graph")
        }
        return topOrder
    }
}