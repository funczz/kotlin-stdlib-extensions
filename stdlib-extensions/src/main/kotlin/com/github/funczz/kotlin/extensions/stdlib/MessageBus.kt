package com.github.funczz.kotlin.extensions.stdlib

import java.util.*
import java.util.concurrent.locks.ReentrantLock
import kotlin.concurrent.withLock

/**
 * メッセージ・バス
 * 参考: java.util.prefs.AbstractPreferences
 */
class MessageBus {

    private var messageDispatchThread: Optional<Thread> = Optional.empty()

    private val messageQueue: LinkedList<MessageEvent> = LinkedList()

    private val messageListeners: LinkedList<MessageListener> = LinkedList()

    private val messageListenersLock = ReentrantLock()

    private val messageQueueLock = ReentrantLock()

    private val messageQueueCondition = messageQueueLock.newCondition()

    fun enqueueEvent(event: MessageEvent) {
        messageQueueLock.withLock {
            messageQueue.add(event)
            messageQueueCondition.signal()
        }
    }

    fun enqueueValue(value: Any) {
        val messageEvent = MessageEvent(messageBus = this, value = value)
        enqueueEvent(messageEvent)
    }

    fun addListener(listener: MessageListener) {
        messageListenersLock.withLock {
            if (!messageListeners.contains(listener)) {
                messageListeners.add(listener)
            }
        }
        startEventDispatchThreadIfNecessary()
    }

    fun removeListener(listener: MessageListener) {
        messageListenersLock.withLock {
            messageListeners.remove(listener)
        }
    }

    private fun startEventDispatchThreadIfNecessary() {
        if (!messageDispatchThread.isPresent) {
            messageDispatchThread = Optional.ofNullable(MessageDispatchThread())
            if (messageDispatchThread.isPresent) {
                val edt = messageDispatchThread.get()
                edt.isDaemon = true
                edt.start()
            }
        }
    }

    /**
     * メッセージ・ディスパッチ・スレッド
     */
    inner class MessageDispatchThread : Thread(
        null as ThreadGroup?,
        null as Runnable?,
        "Message Dispatch Thread",
        0L
    ) {

        override fun run() {
            while (true) {
                val event = messageQueueLock.withLock {
                    try {
                        while (messageQueue.isEmpty()) {
                            messageQueueCondition.await()
                        }
                        messageQueue.removeAt(0)
                    } catch (e: InterruptedException) {
                        return
                    }
                }
                val messageBus = event.source as MessageBus
                for (listener in messageBus.messageListeners) {
                    listener.performed(event)
                }
            }
        }
    }

}