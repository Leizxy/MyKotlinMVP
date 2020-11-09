package cn.leizy.lib.base.mvp

import android.annotation.SuppressLint
import android.util.Log
import androidx.annotation.StringRes
import cn.leizy.lib.http.bean.HttpResponse
import cn.leizy.lib.util.ToastUtil
import cn.leizy.lib.http.Exceptions
import kotlinx.coroutines.*

/**
 * @author Created by wulei
 * @date 2020/9/28, 028
 * @description 引入kotlin协程来进行耗时操作
 * 可以考虑直接写到BasePresenter里面
 */
abstract class BaseCoroutinePresenter<V : IView, M : IModel> : BasePresenter<V, M>() {
    /*    override val coroutineContext: CoroutineContext
            get() = Dispatchers.Main + Job()*/
    private val presenterScope: CoroutineScope by lazy {
        CoroutineScope(Dispatchers.Main + Job())
    }
    private val jobs: MutableList<Job> = mutableListOf()

    protected fun showToast(str: String?) {
        view?.showToast(str)
    }

    @SuppressLint("SupportAnnotationUsage")
    @StringRes
    protected fun showToast(idRes: Int) {
        view?.showToast(idRes)
    }

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
            launch {
                if (showTips) {
                    showToast(it)
                }
            }
        },
        finallyBlock: (suspend CoroutineScope.() -> Unit)? = null,
        isChain: Boolean = false,
        showTips: Boolean = true
    ) {
        if (showTips) {
            view?.showLoading()
        }
        launchMain {
            tryCatch(tryBlock, successBlock, failBlock, finallyBlock, isChain, showTips)
        }
    }

    fun launchMain(block: suspend CoroutineScope.() -> Unit) =
        addJob(presenterScope.launch(Dispatchers.Main, block = block))


    fun launchOnIO(block: suspend CoroutineScope.() -> Unit) =
        addJob(presenterScope.launch(Dispatchers.IO) {
            block()
        })

    fun launchOnDefault(block: suspend CoroutineScope.() -> Unit) =
        addJob(presenterScope.launch(Dispatchers.Default) { block() })

    private fun addJob(job: Job) {
        jobs.add(job)
        job.invokeOnCompletion {
            jobs.remove(job)
        }
    }

    open fun blocking(block: suspend CoroutineScope.() -> Unit) =
        runBlocking(Dispatchers.IO) { block() }

    private suspend fun <T> tryCatch(
        tryBlock: suspend CoroutineScope.() -> HttpResponse<T>?,
        successBlock: suspend CoroutineScope.(T?) -> Unit,
        failBlock: (suspend CoroutineScope.(String?) -> Unit),
        finallyBlock: (suspend CoroutineScope.() -> Unit)? = null,
        isChain: Boolean,
        showTips: Boolean
    ) {
        coroutineScope {
            try {
                val response = tryBlock()
                callResponse(response, {
                    successBlock(response?.Result)
                }, {
                    failBlock(response?.OperationDesc)
                    if (isChain) view?.hideLoading()
                    if (showTips) showToast(response?.OperationDesc)
                })
            } catch (e: Throwable) {
                Exceptions.handleException(e)
                failBlock(e.message)
                if (isChain) view?.hideLoading()
                if (showTips) showToast(e.message)
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
                response?.ResultCode == 200 -> successBlock()
                else -> failBlock()
            }
        }
    }

    override fun detachView() {
        super.detachView()
        if (jobs.size > 0) {
            for (job in jobs) {
                job.cancel()
            }
            jobs.clear()
        }
    }

}