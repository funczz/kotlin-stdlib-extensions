package com.github.funczz.kotlin.extensions.stdlib

import org.w3c.dom.Element
import org.w3c.dom.Node
import java.io.ByteArrayInputStream
import java.io.InputStream
import javax.xml.parsers.DocumentBuilderFactory

/**
 * XML関連のユーティリティクラス。
 */
object XmlUtil {

    /**
     * エレメントを取得する。
     * @param string XML
     * @return Element
     */
    fun getDocumentElement(string: String): Element {
        return getDocumentElement(string.toByteArray())
    }

    /**
     * エレメントを取得する。
     * @param byteArray XML
     * @return Element
     */
    fun getDocumentElement(byteArray: ByteArray): Element {
        return ByteArrayInputStream(byteArray).use {
            getDocumentElement(it)
        }
    }

    /**
     * エレメントを取得する。
     * @param inputStream XML
     * @return Element
     */
    fun getDocumentElement(inputStream: InputStream): Element {
        return DocumentBuilderFactory
            .newInstance()
            .newDocumentBuilder()
            .parse(inputStream)
            .documentElement
    }

    /**
     * エレメントのPCDATA文字列を取得する。
     * @param node  ノード
     * @return String
     */
    fun getTextValue(node: Node): String {
        val nodeList = node.childNodes
        for (i in 0 until nodeList.length) {
            val n = nodeList.item(i)
            if (n.nodeType == Node.TEXT_NODE) {
                return n.nodeValue
            }
        }
        return ""
    }

    /**
     * 名称の一致する子ノードのリストを取得する。
     * @param node 親ノード
     * @param name ノード名称
     * @return 子ノードのリスト
     */
    fun getChildNodesByName(node: Node, name: String): List<Node> {
        val result = mutableListOf<Node>()
        val nodeList = node.childNodes
        for (i in 0 until nodeList.length) {
            val n = nodeList.item(i)
            if (n.nodeType == Node.ELEMENT_NODE && n.nodeName == name) {
                result.add(n)
            }
        }
        return result
    }

}

fun Node.getTextValue(): String {
    return XmlUtil.getTextValue(this)
}

fun Node.getChildNodesByName(name: String): List<Node> {
    return XmlUtil.getChildNodesByName(node = this, name = name)
}
