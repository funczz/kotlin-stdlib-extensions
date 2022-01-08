package com.github.funczz.kotlin.extensions.stdlib

import java.io.NotSerializableException
import java.util.*

/**
 * メッセージ・イベント
 */
class MessageEvent(messageBus: MessageBus, val value: Any) : EventObject(messageBus) {

    fun enqueueEvent() {
        (source as MessageBus).enqueueEvent(event = this)
    }

    @Throws(NotSerializableException::class)
    private fun writeObject() {
        throw NotSerializableException("Not serializable.")
    }

    @Throws(NotSerializableException::class)
    private fun readObject() {
        throw NotSerializableException("Not serializable.")
    }

    companion object {
        private const val serialVersionUID: Long = -5956830545241284109L
    }
}