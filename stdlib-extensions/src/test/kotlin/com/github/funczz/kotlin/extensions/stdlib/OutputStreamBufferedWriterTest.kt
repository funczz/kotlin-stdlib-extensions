package com.github.funczz.kotlin.extensions.stdlib

import io.kotlintest.TestCase
import io.kotlintest.TestResult
import io.kotlintest.shouldBe
import io.kotlintest.specs.StringSpec
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.InputStream

internal class OutputStreamBufferedWriterTest : StringSpec() {

    private val expected = "hello world."

    private lateinit var outputStream: ByteArrayOutputStream

    private lateinit var inputStream: InputStream

    override fun beforeTest(testCase: TestCase) {
        super.beforeTest(testCase)
        outputStream = ByteArrayOutputStream()
        inputStream = ByteArrayInputStream(expected.toByteArray())
    }

    override fun afterTest(testCase: TestCase, result: TestResult) {
        super.afterTest(testCase, result)
        outputStream.close()
        inputStream.close()
    }

    init {

        "OutputStream.write" {
            outputStream.write(inputStream)
            val actual = String(outputStream.toByteArray())

            actual shouldBe expected
        }

        "InputStream.toByteArrayOutputStream" {
            val actual = inputStream.toByteArrayOutputStream().use {
                String(it.toByteArray())
            }

            actual shouldBe expected
        }
    }

}