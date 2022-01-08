package com.github.funczz.kotlin.extensions.stdlib

import java.io.InputStream
import java.io.OutputStream
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit

/**
 * Process.outputStream を返す。
 * @author funczz
 */
fun Process.stdIn(): OutputStream = this.outputStream

/**
 * Process.inputStream を返す。
 * @author funczz
 */
fun Process.stdOut(): InputStream = this.inputStream

/**
 * Process.errorStream を返す。
 * @author funczz
 */
fun Process.stdErr(): InputStream = this.errorStream

/**
 * <code>Process</code> の Stream を取り扱う。
 * @param timeout <code>Process</code> のタイムアウト値。0L 以下ならタイムアウトしない。デフォルト値=0L
 * @param timeUnit <code>TimeUnit</code> タイムアウト値の単位
 * @param stdInExecutor 関数 stdIn を評価する <code>ExecutorService</code>
 * @param stdOutExecutor 関数 stdOut を評価する <code>ExecutorService</code>
 * @param stdErrExecutor 関数 stdErr を評価する <code>ExecutorService</code>
 * @param stdIn 引数として Process.outputStream を持ち、戻り値のない関数
 * @param stdOut 引数として Process.inputStream を持ち、戻り値のない関数
 * @param stdErr 引数として Process.errorStream を持ち、戻り値のない関数
 * @return Result<String> <code>Process</code> が返却する文字列
 * @author funczz
 */
fun Process.capture(
    timeout: Long = 0L,
    timeUnit: TimeUnit = TimeUnit.MILLISECONDS,
    stdInExecutor: ExecutorService = Executors.newSingleThreadExecutor(),
    stdOutExecutor: ExecutorService = Executors.newSingleThreadExecutor(),
    stdErrExecutor: ExecutorService = Executors.newSingleThreadExecutor(),
    stdIn: (OutputStream) -> Unit,
    stdOut: (InputStream) -> Unit,
    stdErr: (InputStream) -> Unit,
): Result<String> {

    val stdInCompletableFuture = stdIn().capture(executor = stdInExecutor, f = stdIn)

    val stdOutCompletableFuture = stdOut().capture(executor = stdOutExecutor, f = stdOut)

    val stdErrCompletableFuture = stdErr().capture(executor = stdErrExecutor, f = stdErr)

    return try {
        when {
            (timeout > 0L) -> this.waitFor(timeout, timeUnit)
            else -> this.waitFor()
        }

        val status = this.exitValue().toString()
        Result.success(status)
    } catch (e: Throwable) {
        Result.failure(e)
    } finally {
        if (this.isAlive) this.destroy()
        if (!stdInCompletableFuture.isDone) {
            stdInCompletableFuture.cancel(true)
        }
        if (!stdOutCompletableFuture.isDone) {
            stdOutCompletableFuture.cancel(true)
        }
        if (!stdErrCompletableFuture.isDone) {
            stdErrCompletableFuture.cancel(true)
        }
    }
}

