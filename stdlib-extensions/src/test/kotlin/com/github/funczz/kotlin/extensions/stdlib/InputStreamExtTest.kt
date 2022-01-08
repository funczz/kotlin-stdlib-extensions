package com.github.funczz.kotlin.extensions.stdlib

import io.kotlintest.TestCase
import io.kotlintest.TestResult
import io.kotlintest.shouldBe
import io.kotlintest.specs.StringSpec
import java.io.ByteArrayInputStream
import java.io.InputStream
import java.nio.charset.Charset
import java.util.concurrent.CancellationException

internal class InputStreamExtTest : StringSpec() {

    private val expected = "hello world."

    private val actual = mutableListOf<String>()

    private var isSuccess = false

    private var throwable: Throwable? = null

    private val expectedStreamLines = """
        $expected
        $expected
        $expected
        """.trimIndent()

    private lateinit var inputStream: InputStream

    override fun beforeTest(testCase: TestCase) {
        super.beforeTest(testCase)
        actual.clear()
        isSuccess = false
        throwable = null
        inputStream = ByteArrayInputStream(expectedStreamLines.toByteArray())
    }

    override fun afterTest(testCase: TestCase, result: TestResult) {
        super.afterTest(testCase, result)
        inputStream.close()
    }

    init {

        "capture(Charset): 正常終了" {
            val completableFuture = inputStream.capture(Charset.defaultCharset()) {
                actual.add(it)
                true
            }
            completableFuture.match(
                failure = {
                    throwable = it
                },
                success = {
                    isSuccess = true
                }
            )
            while (!completableFuture.isDone) {
                Thread.sleep(1L)
            }
            Thread.sleep(10L)

            throwable shouldBe null
            isSuccess shouldBe true
            actual.size shouldBe 3
            actual.forEach {
                it shouldBe expected
            }
        }

        "capture(Charset): 正常終了 (関数 f の戻り値=false)" {
            val completableFuture = inputStream.capture(Charset.defaultCharset()) {
                actual.add(it)
                false
            }
            completableFuture.match(
                failure = {
                    throwable = it
                },
                success = {
                    isSuccess = true
                }
            )
            while (!completableFuture.isDone) {
                Thread.sleep(1L)
            }
            Thread.sleep(10L)

            throwable shouldBe null
            isSuccess shouldBe true
            actual.size shouldBe 1
            actual.forEach {
                it shouldBe expected
            }
        }

        "capture(Charset): キャンセル" {
            val completableFuture = inputStream.capture(Charset.defaultCharset()) {
                actual.add(it)
                Thread.sleep(1000L)
                true
            }
            completableFuture.match(
                failure = {
                    throwable = it
                },
                success = {
                    isSuccess = true
                }
            )
            completableFuture.cancel(true)
            while (!completableFuture.isDone) {
                Thread.sleep(1L)
            }
            Thread.sleep(10L)

            throwable!!::class shouldBe CancellationException::class
            isSuccess shouldBe false
            actual.size shouldBe 1
            actual.forEach {
                it shouldBe expected
            }
        }

        "capture(Charset): 例外エラー" {
            val completableFuture = inputStream.capture(Charset.defaultCharset()) {
                throw IllegalArgumentException("error message")
            }
            var isSuccess = false
            var throwable: Throwable? = null
            completableFuture.match(
                failure = {
                    throwable = it
                },
                success = {
                    isSuccess = true
                }
            )
            while (!completableFuture.isDone) {
                Thread.sleep(1L)
            }
            Thread.sleep(10L)

            throwable!!::class shouldBe IllegalArgumentException::class
            throwable!!.message shouldBe "error message"
            isSuccess shouldBe false
        }

    }

    init {

        "capture: 正常終了" {
            val completableFuture = inputStream.capture {
                it.reader().buffered().forEachLine {
                    actual.add(it)
                }
            }
            completableFuture.match(
                failure = {
                    throwable = it
                },
                success = {
                    isSuccess = true
                }
            )
            while (!completableFuture.isDone) {
                Thread.sleep(1L)
            }
            Thread.sleep(10L)

            throwable shouldBe null
            isSuccess shouldBe true
            actual.size shouldBe 3
            actual.forEach {
                it shouldBe expected
            }
        }

        "capture: キャンセル" {
            val completableFuture = inputStream.capture {
                Thread.sleep(1000L)
                it.reader().buffered().forEachLine {
                    actual.add(it)
                }
            }
            completableFuture.match(
                failure = {
                    throwable = it
                },
                success = {
                    isSuccess = true
                }
            )
            completableFuture.cancel(true)
            while (!completableFuture.isDone) {
                Thread.sleep(1L)
            }
            Thread.sleep(10L)

            throwable!!::class shouldBe CancellationException::class
            isSuccess shouldBe false
            actual.size shouldBe 0
        }

        "capture: 例外エラー" {
            val completableFuture = inputStream.capture {
                throw IllegalArgumentException("error message")
            }
            var isSuccess = false
            var throwable: Throwable? = null
            completableFuture.match(
                failure = {
                    throwable = it
                },
                success = {
                    isSuccess = true
                }
            )
            while (!completableFuture.isDone) {
                Thread.sleep(1L)
            }
            Thread.sleep(10L)

            throwable!!::class shouldBe IllegalArgumentException::class
            throwable!!.message shouldBe "error message"
            isSuccess shouldBe false
        }

    }

}

