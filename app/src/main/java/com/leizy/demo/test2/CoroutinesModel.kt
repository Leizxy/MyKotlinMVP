package com.leizy.demo.test2

import android.util.Log
import cn.leizy.lib.base.mvp.IModel
import cn.leizy.lib.http.bean.HttpResponse
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

/**
 * @author Created by wulei
 * @date 2020/9/28, 028
 * @description
 */
abstract class CoroutinesModel : IModel, CoroutineScope {
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + Job()
    private val launchManager: MutableList<Job> = mutableListOf()

    protected fun <T> launchRequest(
        tryBlock: suspend CoroutineScope.() -> HttpResponse<T>?,
        successBlock: suspend CoroutineScope.(T?) -> Unit,
        failBlock: (suspend CoroutineScope.(String?) -> Unit)? = null,
        finallyBlock: (suspend CoroutineScope.() -> Unit)? = null
    ) {
        Log.i("CoroutinesModel", "launchRequest ${Thread.currentThread().name}")
        launchOnUI() {
            tryCatch(tryBlock, successBlock, failBlock, finallyBlock)
        }
    }

    private fun launchOnUI(block: suspend CoroutineScope.() -> Unit) {
        val job = launch {
            Log.i("CoroutinesModel", "launchOnUI ${Thread.currentThread().name}")
            block()
        }
        launchManager.add(job)
        job.invokeOnCompletion { launchManager.remove(job) }
    }

    private suspend fun <T> tryCatch(
        tryBlock: suspend CoroutineScope.() -> HttpResponse<T>?,
        successBlock: suspend CoroutineScope.(T?) -> Unit,
        failBlock: (suspend CoroutineScope.(String?) -> Unit)? = null,
        finallyBlock: (suspend CoroutineScope.() -> Unit)? = null
    ) {
        coroutineScope {
            Log.i("CoroutinesModel", "tryCatch ${Thread.currentThread().name}")
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
                if (finallyBlock != null) {
                    finallyBlock()
                } else {
                    Log.i("CoroutinesModel", "tryCatch: finally")
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

    override fun cancelHttp() {
        launchManager.clear()
        cancel(cause = CancellationException("cancel"))
/*
        if (launchManager.size != 0) {
            for (job in launchManager) {
                job.cancel(cause = CancellationException("cancel $job"))
            }
            launchManager.clear()
        }
*/
    }
}