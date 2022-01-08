package com.github.funczz.kotlin.extensions.stdlib

import io.kotlintest.TestCase
import io.kotlintest.shouldBe
import io.kotlintest.specs.StringSpec
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

internal class MessageBusTest : StringSpec() {

    private lateinit var messageBus: MessageBus

    private val sleepMilliseconds = 100L

    override fun beforeTest(testCase: TestCase) {
        super.beforeTest(testCase)
        messageBus = MessageBus()
    }

    init {

        "enqueueEvent" {
            val expected = "hello world."
            var actual = ""
            var actual2 = ""

            //addListener
            val listener = MessageListener {
                if (it.value is String) actual = it.value as String
            }
            messageBus.addListener(listener = listener)
            messageBus.addListener {
                if (it.value is String) actual2 = it.value as String
            }

            //enqueueEvent
            val messageEvent = MessageEvent(messageBus = messageBus, value = expected)
            messageEvent.enqueueEvent() //(messageEvent.source as MessageBus).enqueueEvent(event = messageEvent)
            withContext(Dispatchers.IO) {
                Thread.sleep(sleepMilliseconds)
            }

            //result
            actual shouldBe expected
            actual2 shouldBe expected
        }


        "enqueueValue" {
            val expected = 10
            val expected2 = "hello world."
            var actual = 0
            var actual2 = ""

            //addListener
            messageBus.addListener {
                when (val v = it.value) {
                    is Int -> actual += v
                    is String -> actual2 = v
                }
            }

            //enqueueValue
            repeat(10) {
                messageBus.enqueueValue(value = 1)
            }
            messageBus.enqueueValue(value = expected2)
            withContext(Dispatchers.IO) {
                Thread.sleep(sleepMilliseconds)
            }

            //result
            actual shouldBe expected
            actual2 shouldBe expected2
        }

        "removeListener" {
            val expected = ""
            val expected2 = "hello world."
            var actual = ""
            var actual2 = ""

            //addListener
            val listener = MessageListener {
                if (it.value is String) actual = it.value as String
            }
            messageBus.addListener(listener = listener)
            messageBus.addListener {
                if (it.value is String) actual2 = it.value as String
            }

            //removeListener
            messageBus.removeListener(listener = listener)

            //enqueueValue
            messageBus.enqueueValue(value = expected2)
            withContext(Dispatchers.IO) {
                Thread.sleep(sleepMilliseconds)
            }

            //result
            actual shouldBe expected
            actual2 shouldBe expected2
        }

        "order" {
            val expected = "a1a2a3"
            var actual = ""

            //addListener
            messageBus.addListener {
                if (it.value is String) actual = "%s%s1".format(actual, it.value)
            }
            messageBus.addListener {
                if (it.value is String) actual = "%s%s2".format(actual, it.value)
            }
            messageBus.addListener {
                if (it.value is String) actual = "%s%s3".format(actual, it.value)
            }

            //enqueueValue
            messageBus.enqueueValue(value = "a")
            withContext(Dispatchers.IO) {
                Thread.sleep(sleepMilliseconds)
            }

            //result
            actual shouldBe expected
        }
    }
}