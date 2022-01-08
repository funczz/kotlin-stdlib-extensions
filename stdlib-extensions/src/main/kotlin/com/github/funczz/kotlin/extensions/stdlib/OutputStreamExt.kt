package com.github.funczz.kotlin.extensions.stdlib

import java.io.OutputStream
import java.util.concurrent.CompletableFuture
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import java.util.concurrent.FutureTask

/**
 * <code>OutputStream</code> を渡して関数 f を評価する。
 * @param executor 関数 f を評価する Executor
 * @param f 引数として <code>OutputStream</code> を持ち、 戻り値がない関数
 * @return CompletableFuture<Unit>
 * @author funczz
 */
inline fun OutputStream.capture(
    executor: ExecutorService = Executors.newSingleThreadExecutor(),
    crossinline f: (OutputStream) -> Unit
): CompletableFuture<Unit> {
    val futureTask = FutureTask {
        f(this)
    }
    return CompletableFutureBuilder.executeAsync(
        futureTask = futureTask,
        executor = executor,
    )
}