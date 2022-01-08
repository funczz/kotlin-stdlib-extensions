package com.github.funczz.kotlin.extensions.stdlib

import io.kotlintest.TestCase
import io.kotlintest.TestResult
import io.kotlintest.shouldBe
import io.kotlintest.specs.StringSpec
import java.util.*

internal class XMLResourceBundleTest : StringSpec() {

    private val defaultLocale = Locale.getDefault()

    override fun afterTest(testCase: TestCase, result: TestResult) {
        super.afterTest(testCase, result)
        Locale.setDefault(defaultLocale)
    }

    init {

        "ResourceBundle.getBundle(locale = Locale.ENGLISH) - ENGLISH" {
            val bundle = ResourceBundle
                .getBundle("i18n/messages", Locale.ENGLISH, XMLResourceBundleControl())
            bundle.getString("hello_world") shouldBe "hello world."
        }

        "ResourceBundle.getBundle - JAPAN" {
            Locale.setDefault(Locale.JAPAN)
            val bundle = ResourceBundle
                .getBundle("i18n/messages", XMLResourceBundleControl())
            bundle.getString("hello_world") shouldBe "ようこそ世界。"
        }

        "ResourceBundle.getBundle - ENGLISH" {
            Locale.setDefault(Locale.ENGLISH)
            val bundle = ResourceBundle
                .getBundle("i18n/messages", XMLResourceBundleControl())
            bundle.getString("hello_world") shouldBe "hello world."
        }

    }

    init {

        "XMLResourceBundle.getXMLBundle - JAPAN" {
            val bundle = XMLResourceBundle.getXMLBundle("i18n/messages", Locale.JAPAN)
            bundle.getString("hello_world") shouldBe "ようこそ世界。"
        }

        "XMLResourceBundle.getXMLBundle - ENGLISH" {
            val bundle = XMLResourceBundle.getXMLBundle("i18n/messages", Locale.ENGLISH)
            bundle.getString("hello_world") shouldBe "hello world."
        }
    }

}