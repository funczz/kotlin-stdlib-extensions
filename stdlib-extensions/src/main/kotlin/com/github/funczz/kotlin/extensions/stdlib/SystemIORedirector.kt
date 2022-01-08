package com.github.funczz.kotlin.extensions.stdlib

import java.io.InputStream
import java.io.PrintStream

/**
 * システムへの IO 処理を別の Stream へリダイレクトする。
 * @author funczz
 */
interface SystemIORedirector<T> {

    /**
     * リダイレクトした Stream を開放しているなら <code>true</code> を返し、
     * リダイレクトしているなら <code>false</code> を返す。
     */
    fun isReleased(): Boolean

    /**
     * リダイレクトした Stream を開放する。
     */
    fun release()

    /**
     * Stream をリダイレクトする。
     * @param stream リダイレクト先の Stream
     */
    fun redirect(stream: T)

    /**
     * Stream をリダイレクトした状態で関数 f を評価し、
     * 型 R を返却後に Stream を開放する。
     * @param stream リダイレクト先の Stream
     * @param f 引数を持たず、 型 R を返す関数
     * @return R
     */
    fun <R> redirect(stream: T, f: () -> R): R

}

/**
 * System.in をリダイレクトする
 * @author funczz
 */
object SystemInRedirector : SystemIORedirector<InputStream> {

    private val default: InputStream = System.`in`

    override fun isReleased(): Boolean =
        System.`in` === default

    override fun release() {
        if (!isReleased()) System.setIn(default)
    }

    override fun redirect(stream: InputStream) {
        if (System.`in` !== stream) System.setIn(stream)
    }

    override fun <R> redirect(stream: InputStream, f: () -> R): R = try {
        redirect(stream)
        f()
    } finally {
        release()
    }

}

/**
 * System.out をリダイレクトする
 * @author funczz
 */
object SystemOutRedirector : SystemIORedirector<PrintStream> {

    private val default: PrintStream = System.out

    override fun isReleased(): Boolean =
        System.out === default

    override fun release() {
        if (!isReleased()) System.setOut(default)
    }

    override fun redirect(stream: PrintStream) {
        if (System.out !== stream) System.setOut(stream)
    }

    override fun <R> redirect(stream: PrintStream, f: () -> R): R = try {
        redirect(stream)
        val result = f()
        stream.flush()
        result
    } finally {
        release()
    }

}

/**
 * System.err をリダイレクトする
 * @author funczz
 */
object SystemErrRedirector : SystemIORedirector<PrintStream> {

    private val default: PrintStream = System.err

    override fun isReleased(): Boolean =
        System.err === default

    override fun release() {
        if (!isReleased()) System.setErr(default)
    }

    override fun redirect(stream: PrintStream) {
        if (System.err !== stream) System.setErr(stream)
    }

    override fun <R> redirect(stream: PrintStream, f: () -> R): R = try {
        redirect(stream)
        val result = f()
        stream.flush()
        result
    } finally {
        release()
    }

}