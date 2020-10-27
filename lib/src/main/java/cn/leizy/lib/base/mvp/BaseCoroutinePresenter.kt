package cn.leizy.lib.base.mvp

import android.util.Log
import cn.leizy.lib.http.bean.HttpResponse
import cn.leizy.lib.util.ToastUtil
import kotlinx.coroutines.*
import java.net.ConnectException
import java.net.SocketTimeoutException

/**
 * @author Created by wulei
 * @date 2020/9/28, 028
 * @description 引入kotlin协程来进行耗时操作
 * 可以考虑直接写到BasePresenter里面
 */
abstract class BaseCoroutinePresenter<V : IView, M : IModel> : BasePresenter<V, M>() {
    /*override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + Job()*/
    private val presenterScope: CoroutineScope by lazy {
        CoroutineScope(Dispatchers.Main + Job())
    }

    private val jobs: MutableList<Job> = mutableListOf()

    /**
     * @param tryBlock 传入请求的代码块
     * @param successBlock 成功代码块
     * @param failBlock 失败代码块（可不传，采用默认处理）
     * @param finallyBlock 最终处理代码块（可不传）
     * @param isChain 链式调用（为了防止链式调用时暂时别dismiss loading）
     */
    protected fun <T> launchRequest(
        tryBlock: suspend CoroutineScope.() -> HttpResponse<T>?,
        successBlock: suspend CoroutineScope.(T?) -> Unit,
        failBlock: (suspend CoroutineScope.(String?) -> Unit) = {
            launch { ToastUtil.showToast(string = it) }
        },
        finallyBlock: (suspend CoroutineScope.() -> Unit)? = null,
        isChain: Boolean = false
    ) {
        view?.showLoading()
        launchMain {
            tryCatch(tryBlock, successBlock, failBlock, finallyBlock, isChain)
        }
    }

    fun launchMain(block: suspend CoroutineScope.() -> Unit) =
        addJob(presenterScope.launch(Dispatchers.Main, block = block))


    fun launchOnIO(block: suspend CoroutineScope.() -> Unit) =
        addJob(presenterScope.launch(Dispatchers.IO) {
            //            Log.i(this@BaseCoroutinePresenter.javaClass.simpleName, "launchOnUI thread: ${Thread.currentThread().name}")
            //            Log.i(this@BaseCoroutinePresenter.javaClass.simpleName, "launchOnUI thread: ${Thread.currentThread().id}")
            block()
        })

    fun launchOnDefault(block: suspend CoroutineScope.() -> Unit) =
        addJob(presenterScope.launch(Dispatchers.Default) { block() })

    private fun addJob(job: Job) {
        jobs.add(job)
        job.invokeOnCompletion {
            Log.i("BaseCoroutinePresenter", "addJob: remove ${job.key}")
            jobs.remove(job)
        }
    }

    open fun blocking(block: suspend CoroutineScope.() -> Unit) = runBlocking(Dispatchers.IO) { block() }

    private suspend fun <T> tryCatch(
        tryBlock: suspend CoroutineScope.() -> HttpResponse<T>?,
        successBlock: suspend CoroutineScope.(T?) -> Unit,
        failBlock: (suspend CoroutineScope.(String?) -> Unit),
        finallyBlock: (suspend CoroutineScope.() -> Unit)? = null,
        isChain: Boolean
    ) {
        coroutineScope {
            try {
                var response = tryBlock()
                callResponse(response, {
                    successBlock(response?.Result)
                }, { failBlock(response?.OperationDesc) })
            } catch (e: Throwable) {
                // TODO: 2020/10/26, 026 可以加入一个handle处理类
//                ExceptionHandler.handleException(e)
                /*when (e) {
                    is SocketTimeoutException -> {
                        if (!BuildConfig.DEBUG) {
                            ToastUtil.show(BaseApplication.getApplication(), e.message)
                        } else {
                            ToastUtil.show(BaseApplication.getApplication(), R.string.connect_time_out)
                        }
                    }
                    is ConnectException -> {
                        if (!BuildConfig.DEBUG) {
                            ToastUtil.show(BaseApplication.getApplication(), e.message)
                        } else {
                            ToastUtil.show(BaseApplication.getApplication(), R.string.connect_time_fail)
                        }
                    }
                    else -> ToastUtil.show(BaseApplication.getApplication(), e.message)
                }*/
                failBlock(e.message)
            } finally {
                finallyBlock?.let {
                    it()
                }
                if (!isChain) {
                    view?.hideLoading()
                }
            }
        }

    }

    private suspend fun <T> callResponse(
        response: HttpResponse<T>?,
        successBlock: suspend CoroutineScope.() -> Unit,
        failBlock: suspend CoroutineScope.() -> Unit
    ) {
        coroutineScope {
            when {
                response == null -> failBlock()
                response.ResultCode == 200 -> successBlock()
                else -> failBlock()
            }
        }
    }

    override fun detachView() {
        super.detachView()
        Log.i("BaseCoroutinePresenter", "detachView: " + jobs.size)
        if (jobs.size > 0) {
            for (job in jobs) {
                job.cancel()
            }
            jobs.clear()
        }
    }

}