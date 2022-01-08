package com.github.funczz.kotlin.extensions.stdlib

import io.kotlintest.TestCase
import io.kotlintest.TestResult
import io.kotlintest.shouldBe
import io.kotlintest.specs.StringSpec
import java.io.ByteArrayOutputStream
import java.util.concurrent.CancellationException

internal class OutputStreamExtTest : StringSpec() {

    private val expected = "hello world."

    private lateinit var outputStream: ByteArrayOutputStream

    private var isSuccess = false

    private var throwable: Throwable? = null

    override fun beforeTest(testCase: TestCase) {
        super.beforeTest(testCase)
        outputStream = ByteArrayOutputStream()
    }

    override fun afterTest(testCase: TestCase, result: TestResult) {
        super.afterTest(testCase, result)
        outputStream.close()
    }

    init {

        "capture: 正常終了" {
            val completableFuture = outputStream.capture {
                it.write(expected.toByteArray())
                it.flush()
            }
            while (!completableFuture.isDone) {
                Thread.sleep(1L)
            }
            Thread.sleep(10L)
            val actual = String(outputStream.toByteArray())

            actual shouldBe expected
        }

        "capture: キャンセル" {
            val completableFuture = outputStream.capture {
                Thread.sleep(1000L)
                it.write(expected.toByteArray())
                it.flush()
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
        }

        "capture: 例外エラー" {
            val completableFuture = outputStream.capture {
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