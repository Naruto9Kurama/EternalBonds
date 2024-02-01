package com.creator.common.utils

import kotlinx.coroutines.*


/**
 * 协程工具类
 */
object CoroutineUtils {
    // 主线程上运行
    fun runOnMainThread(timeMillis: Long? = null, block: suspend CoroutineScope.() -> Unit) {
        GlobalScope.launch(Dispatchers.Main) {
            if (timeMillis != null) delay(timeMillis)
            block.invoke(this)
        }
    }

    // 后台线程上运行
    fun runOnBackgroundThread(
        timeMillis: Long? = null,
        block: suspend CoroutineScope.() -> Unit,
    ) {
        GlobalScope.launch(Dispatchers.IO) {
            if (timeMillis != null) delay(timeMillis)
            block.invoke(this)
        }
    }

    // 延迟执行
    fun delayExecution(timeMillis: Long, block: suspend CoroutineScope.() -> Unit) {
        runOnBackgroundThread(timeMillis, block)
    }

    // 在主线程延迟执行
    fun delayExecutionOnMainThread(timeMillis: Long, block: suspend CoroutineScope.() -> Unit) {
        runOnMainThread(timeMillis, block)
    }

    // 顺序执行
    fun runSequentially(vararg blocks: suspend CoroutineScope.() -> Unit) {
        GlobalScope.launch {
            blocks.forEach {
                it.invoke(this)
            }
        }
    }

    // 并行执行
    fun runInParallel(vararg blocks: suspend CoroutineScope.() -> Unit) {
        runBlocking {
            val jobs = mutableListOf<Job>()
            blocks.forEach {
                jobs.add(launch {
                    it.invoke(this)
                })
            }
            jobs.forEach { it.join() }
        }
    }
}
