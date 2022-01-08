package com.github.funczz.kotlin.extensions.stdlib

import java.io.IOException
import java.io.InputStream
import java.net.URL
import java.util.*

/**
 * XMLファイルから構築されるリソースバンドル
 * 引数のストリームは閉じられる
 * @param inputStream 読み込み元の Stream
 * @throws IOException
 * @author funczz
 */
class XMLResourceBundle internal constructor(inputStream: InputStream) : ResourceBundle() {

    /**
     * XMLリソースバンドルのバックエンド
     */
    private val properties = Properties()

    init {
        //loadFromXML: 引数のストリームはクローズされる
        properties.loadFromXML(inputStream)
    }

    override fun handleGetObject(key: String): Any {
        return properties.getProperty(key)
    }

    override fun getKeys(): Enumeration<String> {
        @Suppress("UNCHECKED_CAST")
        return properties.keys() as Enumeration<String>
    }

    companion object {

        /**
         * XMLファイルによるリソースバンドルを取得する
         *
         * <pre>{@code
         *    val bundle = XMLResourceBundle.getXMLBundle("i18n/messages", Locale.ENGLISH)
         * }</pre>
         *
         * @param fileName ファイル名
         * @param locale ロケール
         * @param loader クラスローダー (デフォルト: null)
         * @return ResourceBundle
         */
        @JvmStatic
        fun getXMLBundle(fileName: String, locale: Locale, loader: ClassLoader? = null): ResourceBundle =
            if (loader != null) {
                ResourceBundle.getBundle(fileName, locale, loader, XMLResourceBundleControl())
            } else {
                ResourceBundle.getBundle(fileName, locale, XMLResourceBundleControl())
            }

    }

}

/**
 * XMLファイルによるリソースバンドルの扱いを可能にするコントローラ
 * @author funczz
 */
class XMLResourceBundleControl : ResourceBundle.Control() {

    var time = 1000L

    override fun getTimeToLive(baseName: String, locale: Locale): Long {
        return time // 1秒間(デフォルト)でキャッシュ切れ
    }

    /**
     * 対応するフォーマットを返す.
     */
    override fun getFormats(baseName: String): List<String> {
        return listOf(EXT)
    }

    /**
     * 指定されたクラスローダから、名前、ロケール情報でリソースを選択し、
     * リソースバンドルを返す。
     * http://docs.oracle.com/javase/6/docs/api/java/util/ResourceBundle.Control.html
     *
     * @throws IllegalAccessException
     * @throws InstantiationException
     * @throws IOException
     */
    override fun newBundle(
        baseName: String,
        locale: Locale,
        format: String,
        loader: ClassLoader?,
        reload: Boolean
    ): ResourceBundle? {
        return when (format) {
            EXT -> {
                /**
                 * xml形式の場合
                 */

                // ロケールと結合したリソース名を求める
                val bundleName = toBundleName(baseName, locale)

                // 対応するフォーマットと結合したリソース名を求める
                val resourceName = toResourceName(bundleName, format)

                // リソース名をクラスローダから取得する. (なければnull)
                val url: URL? = loader?.getResource(resourceName)
                //println("load resource bundle: $resourceName=$url") //確認用

                val inputStream = url?.openConnection()?.let { connection ->
                    if (reload) {
                        // リロードの場合はキャッシュを無効にしてロードを試みる
                        connection.useCaches = false
                    }
                    connection.getInputStream()
                }

                // 取得されたリソースからXMLリソースバンドルとして読み込む
                inputStream?.use { XMLResourceBundle(it.buffered()) }
            }

            else -> null
        }
    }

    companion object {
        /**
         * 対応するフォーマット形式
         */
        private const val EXT = "xml"
    }

}
