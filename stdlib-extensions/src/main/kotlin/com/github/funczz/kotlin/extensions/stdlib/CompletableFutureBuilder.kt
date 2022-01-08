package com.github.funczz.kotlin.extensions.stdlib

import java.util.concurrent.*

/**
 * キャンセル可能な <code>CompletableFuture</code>。
 * <code>CompletableFuture</code> のキャンセル処理を検出して <code>FutureTask</code> に自身のキャンセル処理を行わせる。
 */
object CompletableFutureBuilder {

    /**
     * ExecutorService.execute を実行
     * @param executor <code>ExecutorService</code>
     *
     * @return <code>CompletableFuture</code>
     */
    fun <T> executeAsync(
        futureTask: FutureTask<T>,
        executor: ExecutorService,
    ): CompletableFuture<T> {
        val completableFuture = createCompletableFuture<T>()
        executor.execute { updateCompletableFuture(futureTask, completableFuture) }
        return completableFuture
    }

    /**
     * ScheduledExecutorService.schedule を実行
     *
     * @param executor <code>ScheduledExecutorService</code>
     * @param delay <code>Long</code> 処理開始前の待機時間
     * @param unit <code>TimeUnit</code>
     *
     * @return <code>CompletableFuture</code>
     */
    fun <T> scheduleAsync(
        futureTask: FutureTask<T>,
        executor: ScheduledExecutorService,
        delay: Long,
        unit: TimeUnit,
    ): CompletableFuture<T> {
        val completableFuture = createCompletableFuture<T>()
        executor.schedule({ updateCompletableFuture(futureTask, completableFuture) }, delay, unit)
        return completableFuture
    }

    /**
     * ScheduledExecutorService.scheduleAtFixedRate を実行
     *
     * @param executor <code>ScheduledExecutorService</code>
     * @param initialDelay <code>Long</code> 処理開始前の待機時間
     * @param period <code>Long</code> 処理開始後の待機時間
     * @param unit <code>TimeUnit</code>
     *
     * @return <code>CompletableFuture</code>
     */
    fun <T> scheduleAtFixedRateAsync(
        futureTask: FutureTask<T>,
        executor: ScheduledExecutorService,
        initialDelay: Long,
        period: Long,
        unit: TimeUnit,
    ): CompletableFuture<T> {
        val completableFuture = createCompletableFuture<T>()
        executor.scheduleAtFixedRate(
            { updateCompletableFuture(futureTask, completableFuture) },
            initialDelay,
            period,
            unit
        )
        return completableFuture
    }

    /**
     * ScheduledExecutorService.scheduleWithFixedDelay を実行
     *
     * @param executor <code>ScheduledExecutorService</code>
     * @param initialDelay <code>Long</code> 処理開始前の待機時間
     * @param delay <code>Long</code> 処理完了後の待機時間
     * @param unit <code>TimeUnit</code>
     *
     * @return <code>CompletableFuture</code>
     */
    fun <T> scheduleWithFixedDelayAsync(
        futureTask: FutureTask<T>,
        executor: ScheduledExecutorService,
        initialDelay: Long,
        delay: Long,
        unit: TimeUnit,
    ): CompletableFuture<T> {
        val completableFuture = createCompletableFuture<T>()
        executor.scheduleWithFixedDelay(
            { updateCompletableFuture(futureTask, completableFuture) },
            initialDelay,
            delay,
            unit
        )
        return completableFuture
    }

    /**
     * XXXAsync メソッドで返却される <code>CompletableFuture} オブジェクトを作成する
     */
    private fun <T> createCompletableFuture(): CompletableFuture<T> {
        return CompletableFuture<T>()
    }

    /**
     * XXXAsync メソッドで返却される <code>CompletableFuture</code> オブジェクトを更新する。
     * 引数 completableFuture に新規の <code>CompletableFuture</code> を代入し、
     * <code>FutureTask</code> の実行結果を適用する。
     */
    private fun <T> updateCompletableFuture(
        futureTask: FutureTask<T>,
        completableFuture: CompletableFuture<T>,
    ) {
        try {
            futureTask.run()
            completableFuture.complete(futureTask.get())
        } catch (e: ExecutionException) {
            completableFuture.completeExceptionally(e.cause)
        } catch (e: Throwable) {
            completableFuture.completeExceptionally(e)
        }

        completableFuture.whenComplete { _, e ->
            if (e is CancellationException && !futureTask.isDone) {
                futureTask.cancel(true)
            }
        }
    }
}

/**
 * <code>CompletableFuture</code> が正常に完了したなら 関数 success を、
 * なんらかの例外が発生したなら 関数 failure を評価する <code>CompletableFuture</code> を返す。
 * @param failure 引数に Throwable を持ち、戻り値がない関数
 * @param success 引数に 型 T を持ち、戻り値がない関数
 */
fun <T> CompletableFuture<T>.match(failure: (Throwable) -> Unit = {}, success: (T) -> Unit): CompletableFuture<T> =
    this.whenComplete { t, throwable ->
        if (throwable is Throwable) {
            failure(throwable)
        } else if (t is T) {
            success(t)
        }
    }

/**
 * <code>CompletableFuture</code> が正常に完了したなら 関数 success を、
 * なんらかの例外が発生したなら 関数 failure を評価する <code>CompletableFuture</code> を返す。
 * @param failure 引数に Throwable を持ち、戻り値がない関数
 * @param success 引数に 型 T を持ち、戻り値がない関数
 * @return <code>CompletableFuture</code>
 */
fun <T> CompletableFuture<T>.matchAsync(failure: (Throwable) -> Unit = {}, success: (T) -> Unit): CompletableFuture<T> =
    this.whenCompleteAsync { t, throwable ->
        if (throwable is Throwable) {
            failure(throwable)
        } else if (t is T) {
            success(t)
        }
    }

/**
 * <code>CompletableFuture</code> が正常に完了したなら 関数 success を、
 * なんらかの例外が発生したなら 関数 failure を executor で評価する CompletableFuture<T> を返す。
 * @param failure 引数に Throwable を持ち、戻り値がない関数
 * @param success 引数に 型 T を持ち、戻り値がない関数
 * @return CompletableFuture<T>
 */
fun <T> CompletableFuture<T>.matchAsync(
    executor: Executor,
    failure: (Throwable) -> Unit = {},
    success: (T) -> Unit
): CompletableFuture<T> =
    this.whenCompleteAsync({ t, throwable ->
        if (throwable is Throwable) {
            failure(throwable)
        } else if (t is T) {
            success(t)
        }
    }, executor)
