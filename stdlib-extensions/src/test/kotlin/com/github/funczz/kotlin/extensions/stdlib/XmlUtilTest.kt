package com.github.funczz.kotlin.extensions.stdlib

import io.kotlintest.shouldBe
import io.kotlintest.specs.StringSpec
import org.w3c.dom.Element
import java.io.ByteArrayInputStream
import javax.xml.parsers.DocumentBuilderFactory

internal class XmlUtilTest : StringSpec() {

    private val xml = """
        <?xml version="1.0" encoding="UTF-8"?>
        <doc>
            <p>hello world.</p>
            <p></p>
            <table>
                <tr>
                    <td>title</td>
                </tr>
            </table>
        </doc>
    """.trimIndent()

    private val element: Element = ByteArrayInputStream(xml.toByteArray()).use {
        DocumentBuilderFactory
            .newInstance()
            .newDocumentBuilder()
            .parse(it)
            .documentElement
    }

    init {

        "getDocumentElement(String)" {
            val result = XmlUtil.getDocumentElement(xml)
            result.nodeName shouldBe "doc"
        }

        "getDocumentElement(ByteArray)" {
            val result = XmlUtil.getDocumentElement(xml.toByteArray())
            result.nodeName shouldBe "doc"
        }

        "getDocumentElement(InputStream)" {
            val result = ByteArrayInputStream(xml.toByteArray()).use {
                XmlUtil.getDocumentElement(it.buffered())
            }
            result.nodeName shouldBe "doc"
        }

        "getChildNodesByName" {
            val nodes = element.getChildNodesByName("p")

            nodes.size shouldBe 2
            nodes.forEach { it.nodeName shouldBe "p" }
        }

        "getTextValue" {
            element
                .getChildNodesByName("table")
                .first()
                .getChildNodesByName("tr")
                .first()
                .getChildNodesByName("td")
                .first()
                .getTextValue() shouldBe "title"

        }
    }
}