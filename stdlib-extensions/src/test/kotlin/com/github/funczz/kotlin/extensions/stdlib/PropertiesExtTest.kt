package com.github.funczz.kotlin.extensions.stdlib

import io.kotlintest.shouldBe
import io.kotlintest.specs.StringSpec
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.File
import java.nio.charset.Charset
import java.util.*

class PropertiesExtTest : StringSpec() {

    init {

        "writeXml, readXml" {
            val file = File("./build/PropertiesExtTest.xml")
            Properties().also {
                it["intValue"] = "1"
            }.writeXml(file.outputStream())
            val result = Properties().readXml(file.inputStream())

            result["intValue"] shouldBe "1"
        }

        "read(inputStream: InputStream): Properties" {
            val result = PropertiesExtTest::class.java
                .getResourceAsStream("test.properties")
                .use {
                    Properties().read(it!!.buffered())
                }

            result["intValue"] shouldBe "1"
        }

        "read(reader: BufferedReader): Properties" {
            val result = PropertiesExtTest::class.java
                .getResourceAsStream("test.properties")
                .use {
                    Properties().read(it!!.bufferedReader(Charset.defaultCharset()))
                }

            result["intValue"] shouldBe "1"
        }

        "read(byteArray: ByteArray): Properties" {
            val byteArray = PropertiesExtTest::class.java
                .getResourceAsStream("test.properties")
                .use {
                    it!!.readBytes()
                }
            val result = Properties().read(byteArray)

            result["intValue"] shouldBe "1"
        }

        "write(outputStream: OutputStream): Properties" {
            val properties = Properties()
            properties["intValue"] = "1"
            val byteArray = ByteArrayOutputStream()
                .use {
                    properties.write(it.buffered())
                    it.toByteArray()
                }
            val result = ByteArrayInputStream(byteArray)
                .use { inputStream ->
                    Properties().also { p ->
                        p.load(inputStream.buffered())
                    }
                }

            result["intValue"] shouldBe "1"
        }

        "write(writer: BufferedWriter): Properties" {
            val properties = Properties()
            properties["intValue"] = "1"
            val byteArray = ByteArrayOutputStream()
                .use {
                    properties.write(it.bufferedWriter(Charset.defaultCharset()))
                    it.toByteArray()
                }
            val result = ByteArrayInputStream(byteArray)
                .use { inputStream ->
                    Properties().also { p ->
                        p.load(inputStream.buffered())
                    }
                }

            result["intValue"] shouldBe "1"
        }

        "write: ByteArrayOutputStream" {
            val properties = Properties()
            properties["intValue"] = "1"
            val byteArray = properties.write().use {
                it.toByteArray()
            }
            val result = ByteArrayInputStream(byteArray)
                .use { inputStream ->
                    Properties().also { p ->
                        p.load(inputStream.buffered())
                    }
                }

            result["intValue"] shouldBe "1"
        }

    }
}