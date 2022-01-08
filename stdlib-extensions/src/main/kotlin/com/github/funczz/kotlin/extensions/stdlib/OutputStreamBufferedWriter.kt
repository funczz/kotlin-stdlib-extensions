package com.github.funczz.kotlin.extensions.stdlib

import java.io.ByteArrayOutputStream
import java.io.InputStream
import java.io.OutputStream

/**
 * InputStream から OutputStream へデータを書き込む
 * @author funczz
 */
object OutputStreamBufferedWriter {

    /**
     * InputStream から OutputStream へデータを書き込む
     * @param writer 書き込み先の Stream
     * @param reader 読み込み元の Stream
     * @param bufferSize 読み書きのバッファサイズ
     */
    fun write(writer: OutputStream, reader: InputStream, bufferSize: Int = DEFAULT_BUFFER_SIZE) {
        var size: Int
        val buffer = ByteArray(bufferSize)
        while (reader.read(buffer).also { size = it } > 0) {
            writer.write(buffer, 0, size)
        }
    }

}

/**
 * inputStream のデータを自身に書き込む
 * @param inputStream 読み込み元の Stream
 * @param bufferSize 読み書きのバッファサイズ
 */
fun OutputStream.write(inputStream: InputStream, bufferSize: Int = DEFAULT_BUFFER_SIZE) {
    OutputStreamBufferedWriter.write(writer = this, reader = inputStream, bufferSize = bufferSize)
}

/**
 * 自身のデータを、生成した <code>ByteArrayOutputStream</code> に書き込む
 * @param bufferSize 読み書きのバッファサイズ
 * @return ByteArrayOutputStream
 */
fun InputStream.toByteArrayOutputStream(bufferSize: Int = DEFAULT_BUFFER_SIZE): ByteArrayOutputStream {
    val writer = ByteArrayOutputStream()
    OutputStreamBufferedWriter.write(writer = writer, reader = this, bufferSize = bufferSize)
    return writer
}
