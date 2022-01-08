package com.github.funczz.kotlin.extensions.stdlib

import io.kotlintest.shouldBe
import io.kotlintest.specs.StringSpec
import java.util.concurrent.CancellationException
import java.util.concurrent.Executors
import java.util.concurrent.FutureTask
import java.util.concurrent.TimeUnit

class CompletableFutureBuilderScheduledTest : StringSpec() {

    private val expected = "hello world."

    init {

        "scheduleAsync: 正常終了" {
            val futureTask = FutureTask {
                expected
            }
            val completableFuture = CompletableFutureBuilder.scheduleAsync(
                futureTask = futureTask,
                executor = Executors.newScheduledThreadPool(1),
                delay = 10L,
                unit = TimeUnit.MILLISECONDS,
            )
            completableFuture.isCompletedExceptionally shouldBe false
            completableFuture.isCancelled shouldBe false

            val (actual, throwable) = CompletableFutureTaskTestUtil.whenComplete(completableFuture, false)

            completableFuture.isCancelled shouldBe false
            completableFuture.isCompletedExceptionally shouldBe false
            throwable shouldBe null
            actual shouldBe expected
        }

        "scheduleAsync: キャンセル" {
            val futureTask = FutureTask {
                Thread.sleep(1000L)
                expected
            }
            val completableFuture = CompletableFutureBuilder.scheduleAsync(
                futureTask = futureTask,
                executor = Executors.newScheduledThreadPool(1),
                delay = 10L,
                unit = TimeUnit.MILLISECONDS,
            )
            completableFuture.isCompletedExceptionally shouldBe false
            completableFuture.isCancelled shouldBe false

            val (actual, throwable) = CompletableFutureTaskTestUtil.whenComplete(completableFuture, true)

            completableFuture.isCancelled shouldBe true
            completableFuture.isCompletedExceptionally shouldBe true
            throwable!!::class shouldBe CancellationException::class
            actual shouldBe null
        }

        "scheduleAsync: 例外エラー" {
            val futureTask = FutureTask<String> {
                throw IllegalArgumentException("error message")
            }
            val completableFuture = CompletableFutureBuilder.scheduleAsync(
                futureTask = futureTask,
                executor = Executors.newScheduledThreadPool(1),
                delay = 10L,
                unit = TimeUnit.MILLISECONDS,
            )
            completableFuture.isCompletedExceptionally shouldBe false
            completableFuture.isCancelled shouldBe false

            val (actual, throwable) = CompletableFutureTaskTestUtil.whenComplete(completableFuture, false)

            completableFuture.isCancelled shouldBe false
            completableFuture.isCompletedExceptionally shouldBe true
            throwable!!::class shouldBe IllegalArgumentException::class
            throwable.message shouldBe "error message"
            actual shouldBe null
        }

    }

    init {

        "scheduleAtFixedRateAsync: 正常終了" {
            val futureTask = FutureTask {
                expected
            }
            val completableFuture = CompletableFutureBuilder.scheduleAtFixedRateAsync(
                futureTask = futureTask,
                executor = Executors.newScheduledThreadPool(1),
                initialDelay = 10L,
                period = 10L,
                unit = TimeUnit.MILLISECONDS,
            )
            completableFuture.isCompletedExceptionally shouldBe false
            completableFuture.isCancelled shouldBe false

            val (actual, throwable) = CompletableFutureTaskTestUtil.whenComplete(completableFuture, false)

            completableFuture.isCancelled shouldBe false
            completableFuture.isCompletedExceptionally shouldBe false
            throwable shouldBe null
            actual shouldBe expected
        }

        "scheduleAtFixedRateAsync: キャンセル" {
            val futureTask = FutureTask {
                Thread.sleep(1000L)
                expected
            }
            val completableFuture = CompletableFutureBuilder.scheduleAtFixedRateAsync(
                futureTask = futureTask,
                executor = Executors.newScheduledThreadPool(1),
                initialDelay = 10L,
                period = 10L,
                unit = TimeUnit.MILLISECONDS,
            )
            completableFuture.isCompletedExceptionally shouldBe false
            completableFuture.isCancelled shouldBe false

            val (actual, throwable) = CompletableFutureTaskTestUtil.whenComplete(completableFuture, true)

            completableFuture.isCancelled shouldBe true
            completableFuture.isCompletedExceptionally shouldBe true
            throwable!!::class shouldBe CancellationException::class
            actual shouldBe null
        }

        "scheduleAtFixedRateAsync: 例外エラー" {
            val futureTask = FutureTask<String> {
                throw IllegalArgumentException("error message")
            }
            val completableFuture = CompletableFutureBuilder.scheduleAtFixedRateAsync(
                futureTask = futureTask,
                executor = Executors.newScheduledThreadPool(1),
                initialDelay = 10L,
                period = 10L,
                unit = TimeUnit.MILLISECONDS,
            )
            completableFuture.isCompletedExceptionally shouldBe false
            completableFuture.isCancelled shouldBe false

            val (actual, throwable) = CompletableFutureTaskTestUtil.whenComplete(completableFuture, false)

            completableFuture.isCancelled shouldBe false
            completableFuture.isCompletedExceptionally shouldBe true
            throwable!!::class shouldBe IllegalArgumentException::class
            throwable.message shouldBe "error message"
            actual shouldBe null
        }

    }

    init {

        "scheduleWithFixedDelayAsync: 正常終了" {
            val futureTask = FutureTask {
                expected
            }
            val completableFuture = CompletableFutureBuilder.scheduleWithFixedDelayAsync(
                futureTask = futureTask,
                executor = Executors.newScheduledThreadPool(1),
                initialDelay = 10L,
                delay = 10L,
                unit = TimeUnit.MILLISECONDS,
            )
            completableFuture.isCompletedExceptionally shouldBe false
            completableFuture.isCancelled shouldBe false

            val (actual, throwable) = CompletableFutureTaskTestUtil.whenComplete(completableFuture, false)

            completableFuture.isCancelled shouldBe false
            completableFuture.isCompletedExceptionally shouldBe false
            throwable shouldBe null
            actual shouldBe expected
        }

        "scheduleWithFixedDelayAsync: キャンセル" {
            val futureTask = FutureTask {
                Thread.sleep(1000L)
                expected
            }
            val completableFuture = CompletableFutureBuilder.scheduleWithFixedDelayAsync(
                futureTask = futureTask,
                executor = Executors.newScheduledThreadPool(1),
                initialDelay = 10L,
                delay = 10L,
                unit = TimeUnit.MILLISECONDS,
            )
            completableFuture.isCompletedExceptionally shouldBe false
            completableFuture.isCancelled shouldBe false

            val (actual, throwable) = CompletableFutureTaskTestUtil.whenComplete(completableFuture, true)

            completableFuture.isCancelled shouldBe true
            completableFuture.isCompletedExceptionally shouldBe true
            throwable!!::class shouldBe CancellationException::class
            actual shouldBe null
        }

        "scheduleWithFixedDelayAsync: 例外エラー" {
            val futureTask = FutureTask<String> {
                throw IllegalArgumentException("error message")
            }
            val completableFuture = CompletableFutureBuilder.scheduleWithFixedDelayAsync(
                futureTask = futureTask,
                executor = Executors.newScheduledThreadPool(1),
                initialDelay = 10L,
                delay = 10L,
                unit = TimeUnit.MILLISECONDS,
            )
            completableFuture.isCompletedExceptionally shouldBe false
            completableFuture.isCancelled shouldBe false

            val (actual, throwable) = CompletableFutureTaskTestUtil.whenComplete(completableFuture, false)

            completableFuture.isCancelled shouldBe false
            completableFuture.isCompletedExceptionally shouldBe true
            throwable!!::class shouldBe IllegalArgumentException::class
            throwable.message shouldBe "error message"
            actual shouldBe null
        }

    }

}