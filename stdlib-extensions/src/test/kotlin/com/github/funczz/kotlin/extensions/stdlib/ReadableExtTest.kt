package io.kotlintest.provided.com.github.funczz.kotlin.extensions.stdlib

import com.github.funczz.kotlin.extensions.stdlib.whileLine
import io.kotlintest.TestCase
import io.kotlintest.TestResult
import io.kotlintest.shouldBe
import io.kotlintest.specs.StringSpec
import java.io.ByteArrayInputStream
import java.io.InputStream

class ReadableExtTest : StringSpec() {

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

        "whileLine: 正常終了" {
            inputStream.bufferedReader().whileLine {
                actual.add(it)
                true
            }

            actual.size shouldBe 3
            for (s in actual) {
                s shouldBe expected
            }
        }

        "whileLine: キャンセル (関数 f の戻り値=false)" {
            inputStream.bufferedReader().whileLine {
                actual.add(it)
                false
            }

            actual.size shouldBe 1
            for (s in actual) {
                s shouldBe expected
            }
        }

    }
}