package com.github.funczz.kotlin.extensions.stdlib

import io.kotlintest.TestCase
import io.kotlintest.TestResult
import io.kotlintest.shouldBe
import io.kotlintest.specs.StringSpec
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.InputStream
import java.io.PrintStream

class SystemIORedirectorTest : StringSpec() {

    private val expected = "hello world."

    private lateinit var inStream: InputStream

    private lateinit var outStream: ByteArrayOutputStream

    private lateinit var errStream: ByteArrayOutputStream

    private lateinit var outPrintStream: PrintStream

    private lateinit var errPrintStream: PrintStream

    override fun beforeTest(testCase: TestCase) {
        super.beforeTest(testCase)
        inStream = ByteArrayInputStream(expected.toByteArray())
        outStream = ByteArrayOutputStream()
        errStream = ByteArrayOutputStream()
        outPrintStream = PrintStream(outStream)
        errPrintStream = PrintStream(errStream)
    }

    override fun afterTest(testCase: TestCase, result: TestResult) {
        super.afterTest(testCase, result)
        inStream.close()
        outStream.close()
        errStream.close()
    }

    init {

        "SystemInRedirector" {
            val redirector = SystemInRedirector
            redirector.redirect(inStream) {
                redirector.isReleased() shouldBe false
                String(System.`in`.readBytes()) shouldBe expected
            }
            redirector.isReleased() shouldBe true
        }

        "SystemOutRedirector" {
            val redirector = SystemOutRedirector
            redirector.redirect(outPrintStream) {
                redirector.isReleased() shouldBe false
                print(expected)
            }
            outStream.toString() shouldBe expected
            redirector.isReleased() shouldBe true
        }

        "SystemErrRedirector" {
            val redirector = SystemErrRedirector
            redirector.redirect(errPrintStream) {
                redirector.isReleased() shouldBe false
                System.err.print(expected)
            }
            errStream.toString() shouldBe expected
            redirector.isReleased() shouldBe true
        }

        "SystemInRedirector, SystemOutRedirector" {
            SystemInRedirector.redirect(inStream) {
                SystemOutRedirector.redirect(outPrintStream) {
                    print(String(System.`in`.readBytes()))
                }
            }
            outStream.toString() shouldBe expected
            SystemInRedirector.isReleased() shouldBe true
            SystemOutRedirector.isReleased() shouldBe true
        }

    }


}