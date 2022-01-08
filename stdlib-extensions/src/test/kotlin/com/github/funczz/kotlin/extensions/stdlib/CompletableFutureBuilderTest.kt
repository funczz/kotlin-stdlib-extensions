package com.github.funczz.kotlin.extensions.stdlib

import io.kotlintest.shouldBe
import io.kotlintest.specs.StringSpec
import java.util.concurrent.CancellationException
import java.util.concurrent.Executors
import java.util.concurrent.FutureTask

class CompletableFutureBuilderTest : StringSpec() {

    private val expected = "hello world."

    init {

        "executeAsync: 正常終了" {
            val futureTask = FutureTask {
                expected
            }
            val completableFuture = CompletableFutureBuilder.executeAsync(
                futureTask = futureTask,
                executor = Executors.newSingleThreadExecutor()
            )
            completableFuture.isCompletedExceptionally shouldBe false
            completableFuture.isCancelled shouldBe false

            val (actual, throwable) = CompletableFutureTaskTestUtil.whenComplete(completableFuture, false)

            completableFuture.isCancelled shouldBe false
            completableFuture.isCompletedExceptionally shouldBe false
            throwable shouldBe null
            actual shouldBe expected
        }

        "executeAsync: キャンセル" {
            val futureTask = FutureTask {
                Thread.sleep(1000L)
                expected
            }
            val completableFuture = CompletableFutureBuilder.executeAsync(
                futureTask = futureTask,
                executor = Executors.newSingleThreadExecutor()
            )
            completableFuture.isCompletedExceptionally shouldBe false
            completableFuture.isCancelled shouldBe false

            val (actual, throwable) = CompletableFutureTaskTestUtil.whenComplete(completableFuture, true)

            completableFuture.isCancelled shouldBe true
            completableFuture.isCompletedExceptionally shouldBe true
            throwable!!::class shouldBe CancellationException::class
            actual shouldBe null
        }

        "executeAsync: 例外エラー" {
            val futureTask = FutureTask<String> {
                throw IllegalArgumentException("error message")
            }
            val completableFuture = CompletableFutureBuilder.executeAsync(
                futureTask = futureTask,
                executor = Executors.newSingleThreadExecutor()
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

        "match: 正常終了" {
            val futureTask = FutureTask {
                expected
            }
            val completableFuture = CompletableFutureBuilder.executeAsync(
                futureTask = futureTask,
                executor = Executors.newSingleThreadExecutor()
            )
            completableFuture.isCompletedExceptionally shouldBe false
            completableFuture.isCancelled shouldBe false

            val (actual, throwable) = CompletableFutureTaskTestUtil.match(completableFuture, false)

            completableFuture.isCancelled shouldBe false
            completableFuture.isCompletedExceptionally shouldBe false
            throwable shouldBe null
            actual shouldBe expected
        }

        "match: キャンセル" {
            val futureTask = FutureTask {
                Thread.sleep(1000L)
                expected
            }
            val completableFuture = CompletableFutureBuilder.executeAsync(
                futureTask = futureTask,
                executor = Executors.newSingleThreadExecutor()
            )
            completableFuture.isCompletedExceptionally shouldBe false
            completableFuture.isCancelled shouldBe false

            val (actual, throwable) = CompletableFutureTaskTestUtil.match(completableFuture, true)

            completableFuture.isCancelled shouldBe true
            completableFuture.isCompletedExceptionally shouldBe true
            throwable!!::class shouldBe CancellationException::class
            actual shouldBe null
        }

        "match: 例外エラー" {
            val futureTask = FutureTask<String> {
                throw IllegalArgumentException("error message")
            }
            val completableFuture = CompletableFutureBuilder.executeAsync(
                futureTask = futureTask,
                executor = Executors.newSingleThreadExecutor()
            )
            completableFuture.isCompletedExceptionally shouldBe false
            completableFuture.isCancelled shouldBe false

            val (actual, throwable) = CompletableFutureTaskTestUtil.match(completableFuture, false)

            completableFuture.isCancelled shouldBe false
            completableFuture.isCompletedExceptionally shouldBe true
            throwable!!::class shouldBe IllegalArgumentException::class
            throwable.message shouldBe "error message"
            actual shouldBe null
        }

    }

    init {

        "matchAsync: 正常終了" {
            val futureTask = FutureTask {
                expected
            }
            val completableFuture = CompletableFutureBuilder.executeAsync(
                futureTask = futureTask,
                executor = Executors.newSingleThreadExecutor()
            )
            val (actual, throwable) = CompletableFutureTaskTestUtil.matchAsync(completableFuture, false)

            completableFuture.isCancelled shouldBe false
            completableFuture.isCompletedExceptionally shouldBe false
            throwable shouldBe null
            actual shouldBe expected
        }

        "matchAsync: キャンセル" {
            val futureTask = FutureTask {
                Thread.sleep(1000L)
                expected
            }
            val completableFuture = CompletableFutureBuilder.executeAsync(
                futureTask = futureTask,
                executor = Executors.newSingleThreadExecutor()
            )
            completableFuture.isCompletedExceptionally shouldBe false
            completableFuture.isCancelled shouldBe false

            val (actual, throwable) = CompletableFutureTaskTestUtil.matchAsync(completableFuture, true)

            completableFuture.isCancelled shouldBe true
            completableFuture.isCompletedExceptionally shouldBe true
            throwable!!::class shouldBe CancellationException::class
            actual shouldBe null
        }

        "matchAsync: 例外エラー" {
            val futureTask = FutureTask<String> {
                throw IllegalArgumentException("error message")
            }
            val completableFuture = CompletableFutureBuilder.executeAsync(
                futureTask = futureTask,
                executor = Executors.newSingleThreadExecutor()
            )
            completableFuture.isCompletedExceptionally shouldBe false
            completableFuture.isCancelled shouldBe false

            val (actual, throwable) = CompletableFutureTaskTestUtil.matchAsync(completableFuture, false)

            completableFuture.isCancelled shouldBe false
            completableFuture.isCompletedExceptionally shouldBe true
            throwable!!::class shouldBe IllegalArgumentException::class
            throwable.message shouldBe "error message"
            actual shouldBe null
        }

    }

    init {

        "matchAsync(Executor): 正常終了" {
            val futureTask = FutureTask {
                expected
            }
            val completableFuture = CompletableFutureBuilder.executeAsync(
                futureTask = futureTask,
                executor = Executors.newSingleThreadExecutor()
            )
            completableFuture.isCompletedExceptionally shouldBe false

            val (actual, throwable) = CompletableFutureTaskTestUtil.matchAsync(
                completableFuture,
                false,
                Executors.newSingleThreadExecutor()
            )

            completableFuture.isCancelled shouldBe false
            completableFuture.isCompletedExceptionally shouldBe false
            throwable shouldBe null
            actual shouldBe expected
        }

        "matchAsync(Executor): キャンセル" {
            val futureTask = FutureTask {
                Thread.sleep(1000L)
                expected
            }
            val completableFuture = CompletableFutureBuilder.executeAsync(
                futureTask = futureTask,
                executor = Executors.newSingleThreadExecutor()
            )
            completableFuture.isCompletedExceptionally shouldBe false
            completableFuture.isCancelled shouldBe false

            val (actual, throwable) = CompletableFutureTaskTestUtil.matchAsync(
                completableFuture,
                true,
                Executors.newSingleThreadExecutor()
            )

            completableFuture.isCancelled shouldBe true
            completableFuture.isCompletedExceptionally shouldBe true
            throwable!!::class shouldBe CancellationException::class
            actual shouldBe null
        }

        "matchAsync(Executor): 例外エラー" {
            val futureTask = FutureTask<String> {
                throw IllegalArgumentException("error message")
            }
            val completableFuture = CompletableFutureBuilder.executeAsync(
                futureTask = futureTask,
                executor = Executors.newSingleThreadExecutor()
            )
            completableFuture.isCompletedExceptionally shouldBe false
            completableFuture.isCancelled shouldBe false

            val (actual, throwable) = CompletableFutureTaskTestUtil.matchAsync(
                completableFuture,
                false,
                Executors.newSingleThreadExecutor()
            )

            completableFuture.isCancelled shouldBe false
            completableFuture.isCompletedExceptionally shouldBe true
            throwable!!::class shouldBe IllegalArgumentException::class
            throwable.message shouldBe "error message"
            actual shouldBe null
        }

    }

}