package com.github.funczz.kotlin.extensions.stdlib

import io.kotlintest.matchers.string.shouldEndWith
import io.kotlintest.shouldBe
import io.kotlintest.specs.StringSpec
import java.io.File
import java.util.*

internal class ResourceReaderTest : StringSpec() {

    init {

        "Class<T>.getResource - 引数=test.properties" {
            val result = ResourceReaderTest::class.java.getResource("test.properties")

            result!!.path shouldEndWith "/resources/test/com/github/funczz/kotlin/extensions/stdlib/test.properties"
        }

        "Class<T>.getResourceAsStream - 引数=test.properties" {
            val result: Properties = ResourceReaderTest::class.java
                .getResourceAsStream("test.properties")
                .use { inputStream ->
                    Properties().also {
                        it.load(inputStream!!.buffered())
                    }
                }

            result["intValue"] shouldBe "1"
        }

        "ClassResourceReader.getResource - 引数=test.properties" {
            val result = ClassResourceReader(ResourceReaderTest::class.java)
                .getResource("test.properties")

            result.path shouldEndWith "/resources/test/com/github/funczz/kotlin/extensions/stdlib/test.properties"
        }

        "ClassResourceReader.getResourceAsStream - 引数=test.properties" {
            val result: Properties = ClassResourceReader(ResourceReaderTest::class.java)
                .getResourceAsStream("test.properties").use { inputStream ->
                    Properties().also {
                        it.load(inputStream.buffered())
                    }
                }

            result["intValue"] shouldBe "1"
        }

        "Any.getResource - 引数=test.properties" {
            val result = this@ResourceReaderTest.getResource("test.properties")

            result.path shouldEndWith "/resources/test/com/github/funczz/kotlin/extensions/stdlib/test.properties"
        }

        "Any.getResourceAsStream - 引数=test.properties" {
            val result: Properties = this@ResourceReaderTest
                .getResourceAsStream("test.properties")
                .use { inputStream ->
                    Properties().also {
                        it.load(inputStream.buffered())
                    }
                }

            result["intValue"] shouldBe "1"
        }

    }

    init {

        "ClassLoaderResourceReader().getResource - 引数=com/github/funczz/kotlin/extensions/stdlib/test.properties" {
            val result = ClassLoaderResourceReader()
                .getResource("com/github/funczz/kotlin/extensions/stdlib/test.properties")

            result.path shouldEndWith "/resources/test/com/github/funczz/kotlin/extensions/stdlib/test.properties"
        }

        "ClassLoaderResourceReader.getResourceAsStream - 引数=com/github/funczz/kotlin/extensions/stdlib/test.properties" {
            val result: Properties = ClassLoaderResourceReader()
                .getResourceAsStream("com/github/funczz/kotlin/extensions/stdlib/test.properties")
                .use { inputStream ->
                    Properties().also {
                        it.load(inputStream.buffered())
                    }
                }

            result["intValue"] shouldBe "1"
        }

    }

    init {

        "FileSystemResourceReader.getResource - 引数=src/test/resources/com/github/funczz/kotlin/extensions/stdlib/test.properties" {
            val result = FileSystemResourceReader()
                .getResource("src/test/resources/com/github/funczz/kotlin/extensions/stdlib/test.properties")

            result.path shouldEndWith "stdlib-extensions/src/test/resources/com/github/funczz/kotlin/extensions/stdlib/test.properties"
        }

        "FileSystemResourceReader.getResourceAsStream - 引数=src/test/resources/com/github/funczz/kotlin/extensions/stdlib/test.properties" {
            val result = FileSystemResourceReader()
                .getResourceAsStream("src/test/resources/com/github/funczz/kotlin/extensions/stdlib/test.properties")
                .use { inputStream ->
                    Properties().also {
                        it.load(inputStream.buffered())
                    }
                }

            result["intValue"] shouldBe "1"
        }

    }

    private val uri =
        File("src/test/resources/com/github/funczz/kotlin/extensions/stdlib/test.properties").toPath().toUri()
            .toASCIIString()

    init {

        "URIResourceReader.getResource - 引数=$uri" {
            val result = URIResourceReader()
                .getResource(uri)
            result.path shouldEndWith "src/test/resources/com/github/funczz/kotlin/extensions/stdlib/test.properties"
        }

        "URIResourceReader.getResourceAsStream - 引数=$uri" {
            val result = URIResourceReader()
                .getResourceAsStream(uri)
                .use { inputStream ->
                    Properties().also {
                        it.load(inputStream.buffered())
                    }
                }

            result["intValue"] shouldBe "1"
        }

    }

}