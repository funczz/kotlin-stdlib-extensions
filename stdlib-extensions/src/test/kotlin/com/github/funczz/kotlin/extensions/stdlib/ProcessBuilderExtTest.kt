package com.github.funczz.kotlin.extensions.stdlib

import io.kotlintest.Spec
import io.kotlintest.TestCase
import io.kotlintest.matchers.collections.shouldContain
import io.kotlintest.matchers.numerics.shouldBeGreaterThan
import io.kotlintest.matchers.numerics.shouldBeGreaterThanOrEqual
import io.kotlintest.shouldBe
import io.kotlintest.specs.StringSpec
import java.io.File
import java.io.IOException
import java.util.concurrent.Executors

internal class ProcessBuilderExtTest : StringSpec() {

    private val executor = Executors.newWorkStealingPool()

    private val stdOutValue = mutableListOf<String>()

    private val stdErrValue = mutableListOf<String>()

    override fun beforeTest(testCase: TestCase) {
        super.beforeTest(testCase)
        stdOutValue.clear()
        stdErrValue.clear()
    }

    override fun afterSpec(spec: Spec) {
        super.afterSpec(spec)
        executor.shutdown()
    }

    init {

        "ディレクトリを指定して ls を実行" {
            val result = ProcessBuilder()
                .command("ls", "-a", "-1")
                .directory(File("./src"))
                .capture(
                    stdInExecutor = executor,
                    stdOutExecutor = executor,
                    stdErrExecutor = executor,
                    stdIn = {
                        it.close()
                    },
                    stdOut = {
                        it.reader().buffered().forEachLine {
                            stdOutValue.add(it)
                        }
                    },
                    stdErr = {
                        it.reader().buffered().forEachLine {
                            stdErrValue.add(it)
                        }
                    },
                )

            result.isSuccess shouldBe true
            result.getOrThrow() shouldBe "0"
            stdOutValue shouldContain "."
            stdOutValue shouldContain "main"
            stdErrValue.size shouldBe 0
        }

        "認証エラー sudo --user=unknown_user -S ls" {
            val result = ProcessBuilder()
                .command("sudo", "--user=unknown_user", "-S", "ls")
                .capture(
                    stdInExecutor = executor,
                    stdOutExecutor = executor,
                    stdErrExecutor = executor,
                    stdIn = {
                        it.close()
                    },
                    stdOut = {
                        it.reader().buffered().forEachLine {
                            stdOutValue.add(it)
                        }
                    },
                    stdErr = {
                        it.reader().buffered().forEachLine {
                            stdErrValue.add(it)
                        }
                    },
                )

            result.isSuccess shouldBe true
            result.getOrThrow() shouldBe "1"
            stdOutValue.size shouldBe 0
            stdErrValue.forEach { println("err: $it") }
            stdErrValue.size shouldBeGreaterThanOrEqual 1
            stdErrValue.filter { ".*sudo:.+".toRegex().matches(it) }.size shouldBeGreaterThanOrEqual 1
        }

        "タイムアウト" {
            val result = ProcessBuilder()
                .command("sudo", "-S", "ls")
                .capture(
                    timeout = 1L,
                    stdInExecutor = executor,
                    stdOutExecutor = executor,
                    stdErrExecutor = executor,
                    stdIn = {
                    },
                    stdOut = {
                        it.reader().buffered().forEachLine {
                            stdOutValue.add(it)
                        }
                    },
                    stdErr = {
                        it.reader().buffered().forEachLine {
                            stdErrValue.add(it)
                        }
                    },
                )

            result.isSuccess shouldBe false
            result.exceptionOrNull()!!.also {
                it::class shouldBe IllegalThreadStateException::class
                it.message shouldBe "process hasn't exited"
                it.cause shouldBe null
            }
            stdOutValue.size shouldBe 0
            stdErrValue.size shouldBe 0
        }

        "コマンド指定エラー" {
            val result = ProcessBuilder()
                .command("NoSuchFile")
                .capture(
                    stdInExecutor = executor,
                    stdOutExecutor = executor,
                    stdErrExecutor = executor,
                    stdIn = {
                        it.close()
                    },
                    stdOut = {
                        it.reader().buffered().forEachLine {
                            stdOutValue.add(it)
                        }
                    },
                    stdErr = {
                        it.reader().buffered().forEachLine {
                            stdErrValue.add(it)
                        }
                    },
                )

            result.isSuccess shouldBe false
            result.exceptionOrNull()!!.also {
                it::class shouldBe IOException::class
                it.message shouldBe """Cannot run program "NoSuchFile": error=2, No such file or directory"""
                it.cause!!.also {
                    it::class shouldBe IOException::class
                    it.message shouldBe "error=2, No such file or directory"
                    it.cause shouldBe null
                }
            }

        }

    }

}