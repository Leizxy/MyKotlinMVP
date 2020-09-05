package cn.leizy.lauch

import java.util.concurrent.*
import java.util.concurrent.atomic.AtomicInteger

/**
 * @author Created by wulei
 * @date 2020/8/6, 006
 * @description
 */
object DispatcherExecutor {
    private val CPU_COUNT: Int = Runtime.getRuntime().availableProcessors()

    // We want at least 2 threads and at most 4 threads in the core pool,
    // preferring to have 1 less than the CPU count to avoid saturating
    // the CPU with background work
    val CORE_POOL_SIZE = Math.max(2, Math.min(CPU_COUNT - 1, 5))

    private val MAXIMUM_POOL_SIZE = CORE_POOL_SIZE
    private val KEEP_ALIVE_SECONDS = 5
    private val poolWorkQueue: BlockingQueue<Runnable> = LinkedBlockingQueue()
    private val threadFactory: DefaultThreadFactory = DefaultThreadFactory()
    private val handler: RejectedExecutionHandler = RejectedExecutionHandler { r, executor ->
        Executors.newCachedThreadPool().execute(r)
    }
    var cpuThreadPoolExecutor: ThreadPoolExecutor? = null
    var ioThreadPoolExecutor: ExecutorService

    init {
        cpuThreadPoolExecutor = ThreadPoolExecutor(CORE_POOL_SIZE, MAXIMUM_POOL_SIZE, KEEP_ALIVE_SECONDS.toLong(), TimeUnit.SECONDS, poolWorkQueue, threadFactory, handler)
        cpuThreadPoolExecutor!!.allowCoreThreadTimeOut(true)
        ioThreadPoolExecutor = Executors.newCachedThreadPool(threadFactory)
    }

    private class DefaultThreadFactory : ThreadFactory {
        companion object {
            private val poolNumber: AtomicInteger = AtomicInteger(1)
        }

        private val group: ThreadGroup
        private val threadNumber: AtomicInteger = AtomicInteger(1)
        private val namePrefix: String


        init {
            val s = SecurityManager()
            group = s.threadGroup
            namePrefix = "TaskDispatcherPool-" + poolNumber.getAndIncrement() + "-Thread-"
        }

        override fun newThread(r: Runnable?): Thread {
            val t = Thread(group, r, namePrefix + threadNumber.getAndIncrement(), 0)
            if (t.isDaemon) {
                t.isDaemon = false
            }
            if (t.priority != Thread.NORM_PRIORITY) {
                t.priority = Thread.NORM_PRIORITY
            }
            return t
        }

    }
}