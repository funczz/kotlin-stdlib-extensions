package com.github.funczz.kotlin.extensions.stdlib

import java.util.*

/**
 * <code>Readable</code> の文字列を行単位で取得して関数 f を評価する。
 * 関数 f の戻り値が <code>true</code> なら、処理を継続し、
 * <code>false</code> なら 処理を終了する。
 * @param f 引数として <code>String</code> を持ち、 <code>Boolean</code> を返す関数
 * @author funczz
 */
inline fun Readable.whileLine(
    crossinline f: (String) -> Boolean
) {
    val sc = Scanner(this)
    while (sc.hasNextLine()) {
        if (!f(sc.nextLine())) break
    }
}