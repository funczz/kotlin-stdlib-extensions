package com.github.funczz.kotlin.extensions.stdlib

import java.io.InputStream
import java.nio.charset.Charset
import java.util.concurrent.CompletableFuture
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import java.util.concurrent.FutureTask

/**
 * <code>InputStream</code> に渡された文字列を行単位で取得して関数 f を評価する。
 * 関数 f の戻り値が <code>true</code> なら、処理を継続し、
 * <code>false</code> なら 処理を終了する。
 * @param charset <code>Charset</code>
 * @param executor 文字列を処理する Executor
 * @param f 引数として <code>String</code> を持ち、 <code>Boolean</code> を返す関数
 * @return CompletableFuture<Unit>
 * @author funczz
 */
inline fun InputStream.capture(
    charset: Charset,
    executor: ExecutorService = Executors.newSingleThreadExecutor(),
    crossinline f: (String) -> Boolean
): CompletableFuture<Unit> {
    val futureTask = FutureTask {
        this.bufferedReader(charset).whileLine(f)
    }
    return CompletableFutureBuilder.executeAsync(
        futureTask = futureTask,
        executor = executor,
    )
}

/**
 * <code>InputStream</code> を渡して関数 f を評価する。
 * @param executor 関数 f を評価する Executor
 * @param f 引数として <code>InputStream</code> を持ち、 戻り値がない関数
 * @return CompletableFuture<Unit>
 * @author funczz
 */
inline fun InputStream.capture(
    executor: ExecutorService = Executors.newSingleThreadExecutor(),
    crossinline f: (InputStream) -> Unit
): CompletableFuture<Unit> {
    val futureTask = FutureTask {
        f(this)
    }
    return CompletableFutureBuilder.executeAsync(
        futureTask = futureTask,
        executor = executor,
    )
}