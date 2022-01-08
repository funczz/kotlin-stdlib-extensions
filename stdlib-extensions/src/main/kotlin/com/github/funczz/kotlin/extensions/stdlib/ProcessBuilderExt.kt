package com.github.funczz.kotlin.extensions.stdlib

import java.io.InputStream
import java.io.OutputStream
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit

/**
 * <code>Process</code> を生成して Stream を取り扱う。
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
fun ProcessBuilder.capture(
    timeout: Long = 0L,
    timeUnit: TimeUnit = TimeUnit.MILLISECONDS,
    stdInExecutor: ExecutorService = Executors.newSingleThreadExecutor(),
    stdOutExecutor: ExecutorService = Executors.newSingleThreadExecutor(),
    stdErrExecutor: ExecutorService = Executors.newSingleThreadExecutor(),
    stdIn: (OutputStream) -> Unit,
    stdOut: (InputStream) -> Unit,
    stdErr: (InputStream) -> Unit,
): Result<String> = try {
    this.start().capture(
        timeout = timeout,
        timeUnit = timeUnit,
        stdInExecutor = stdInExecutor,
        stdOutExecutor = stdOutExecutor,
        stdErrExecutor = stdErrExecutor,
        stdIn = stdIn,
        stdOut = stdOut,
        stdErr = stdErr,
    )
} catch (e: Throwable) {
    Result.failure(e)
}
