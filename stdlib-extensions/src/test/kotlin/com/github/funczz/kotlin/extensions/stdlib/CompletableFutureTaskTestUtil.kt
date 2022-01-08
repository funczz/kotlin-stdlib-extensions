package com.github.funczz.kotlin.extensions.stdlib

import java.util.concurrent.CompletableFuture
import java.util.concurrent.Executor

object CompletableFutureTaskTestUtil {

    fun <T> whenComplete(completableFuture: CompletableFuture<T>, cancel: Boolean = false): Pair<T?, Throwable?> {
        var throwable: Throwable? = null
        var actual: T? = null
        completableFuture.whenComplete { t, e ->
            when (e is Throwable) {
                true -> {
                    throwable = e
                }
                else -> {
                    actual = t
                }
            }
        }
        if (cancel) completableFuture.cancel(true)
        while (!completableFuture.isDone) {
            Thread.sleep(1L)
        }
        Thread.sleep(10L)
        return Pair(actual, throwable)
    }

    fun <T> match(completableFuture: CompletableFuture<T>, cancel: Boolean = false): Pair<T?, Throwable?> {
        var throwable: Throwable? = null
        var actual: T? = null
        completableFuture.match(
            failure = {
                throwable = it
            },
            success = {
                actual = it
            }
        )
        if (cancel) completableFuture.cancel(true)
        while (!completableFuture.isDone) {
            Thread.sleep(1L)
        }
        Thread.sleep(10L)
        return Pair(actual, throwable)
    }

    fun <T> matchAsync(completableFuture: CompletableFuture<T>, cancel: Boolean = false): Pair<T?, Throwable?> {
        var throwable: Throwable? = null
        var actual: T? = null
        val completableFuture2 = completableFuture.matchAsync(
            failure = {
                throwable = it
            },
            success = {
                actual = it
            }
        )
        if (cancel) completableFuture.cancel(true)
        while (!completableFuture2.isDone) {
            Thread.sleep(1L)
        }
        Thread.sleep(10L)
        return Pair(actual, throwable)
    }

    fun <T> matchAsync(
        completableFuture: CompletableFuture<T>,
        cancel: Boolean = false,
        executor: Executor
    ): Pair<T?, Throwable?> {
        var throwable: Throwable? = null
        var actual: T? = null
        val completableFuture2 = completableFuture.matchAsync(
            executor = executor,
            failure = {
                throwable = it
            },
            success = {
                actual = it
            }
        )
        if (cancel) completableFuture.cancel(true)
        while (!completableFuture2.isDone) {
            Thread.sleep(1L)
        }
        Thread.sleep(10L)
        return Pair(actual, throwable)
    }
}