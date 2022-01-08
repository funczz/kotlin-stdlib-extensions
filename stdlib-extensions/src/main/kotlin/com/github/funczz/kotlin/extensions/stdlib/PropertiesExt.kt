package com.github.funczz.kotlin.extensions.stdlib

import java.io.*
import java.util.*

/**
 * プロパティを <code>InputStream</code> から読み込む
 * @param inputStream 読み込み元の Stream
 * @return Properties 自身を返す
 * @author funczz
 */
fun Properties.read(inputStream: InputStream): Properties {
    this.load(inputStream)
    return this
}

/**
 * プロパティを <code>BufferedReader</code> から読み込む
 * @param reader 読み込み元の BufferedReader
 * @return Properties 自身を返す
 * @author funczz
 */
fun Properties.read(reader: BufferedReader): Properties {
    this.load(reader)
    return this
}

/**
 * プロパティを <code>ByteArray</code> から読み込む
 * @param byteArray 読み込み元の ByteArray
 * @return Properties 自身を返す
 * @author funczz
 */
fun Properties.read(byteArray: ByteArray): Properties {
    ByteArrayInputStream(byteArray)
        .use { inputStream ->
            this.load(inputStream.buffered())
        }
    return this
}

/**
 * プロパティを <code>OutputStream</code> へ書き込む
 * @param outputStream 書き込み先の Stream
 * @param comment Properties.store に指定するコメント
 * @return Properties 自身を返す
 * @author funczz
 */
fun Properties.write(outputStream: OutputStream, comment: String = ""): Properties {
    this.store(outputStream, comment)
    return this
}

/**
 * プロパティを <code>BufferedWriter</code> へ書き込む
 * @param writer 書き込み先の Writer
 * @param comment Properties.store に指定するコメント
 * @return Properties 自身を返す
 * @author funczz
 */
fun Properties.write(writer: BufferedWriter, comment: String = ""): Properties {
    this.store(writer, comment)
    return this
}

/**
 * プロパティを、生成した <code>ByteArrayOutputStream</code> に書き込む
 * @param comment Properties.store に指定するコメント
 * @return ByteArrayOutputStream
 * @author funczz
 */
fun Properties.write(comment: String = ""): ByteArrayOutputStream {
    val byteArrayOutputStream = ByteArrayOutputStream()
    this.store(byteArrayOutputStream.buffered(), comment)
    return byteArrayOutputStream
}

