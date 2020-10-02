package cn.leizy.lib.base.mvp

import android.util.Log
import cn.leizy.lib.http.bean.HttpResponse
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

/**
 * @author Created by wulei
 * @date 2020/9/28, 028
 * @description 引入kotlin协程来进行耗时操作
 * 可以考虑直接写到BasePresenter里面
 */
abstract class BaseCoroutinePresenter<V : IView, M : IModel> : BasePresenter<V, M>(),
    CoroutineScope {
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + Job()

    protected fun <T> launchRequest(
        tryBlock: suspend CoroutineScope.() -> HttpResponse<T>?,
        successBlock: suspend CoroutineScope.(T?) -> Unit,
        failBlock: (suspend CoroutineScope.(String?) -> Unit)? = { e: String? ->
            launchMain { Log.i(this.javaClass.simpleName, "failBlock: $e") }
        },
        finallyBlock: (suspend CoroutineScope.() -> Unit)? = {
            launchMain { Log.i(this.javaClass.simpleName, "finallyBlock: ") }
        }
    ) {
        Log.i(this.javaClass.simpleName, "launchRequest ${Thread.currentThread().name}")
        launchOnUI {
            tryCatch(tryBlock, successBlock, failBlock, finallyBlock)
        }
    }

    protected fun launchMain(block: suspend CoroutineScope.() -> Unit) {
        launch(Dispatchers.Main, block = block)
    }

    private fun launchOnUI(block: suspend CoroutineScope.() -> Unit) {
        launch(Dispatchers.IO) {
            Log.i(this.javaClass.simpleName, "launchOnUI ${Thread.currentThread().name}")
            block()
        }
    }

    private suspend fun <T> tryCatch(
        tryBlock: suspend CoroutineScope.() -> HttpResponse<T>?,
        successBlock: suspend CoroutineScope.(T?) -> Unit,
        failBlock: (suspend CoroutineScope.(String?) -> Unit)? = null,
        finallyBlock: (suspend CoroutineScope.() -> Unit)? = null
    ) {
        coroutineScope {
            Log.i(this.javaClass.simpleName, "tryCatch ${Thread.currentThread().name}")
            try {
                var response = tryBlock()
                callResponse(response, {
                    successBlock(response?.Result)
                }, {
                    failBlock?.let { it(response?.OperationDesc) }
                })
            } catch (e: Throwable) {
                failBlock?.let { it(e.message) }
            } finally {
                finallyBlock?.let { it() }
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
        cancel(cause = CancellationException("${this::class.java} cancel"))
    }

}