package com.github.funczz.kotlin.extensions.stdlib

import java.io.File
import java.io.InputStream
import java.net.URI
import java.net.URL
import java.nio.file.FileSystem
import java.nio.file.FileSystems
import java.nio.file.Path

/**
 * リソースを取得する
 * @author funczz
 */
interface ResourceReader {

    /**
     * リソースを取得する
     * @param name リソース名
     * @return URL
     */
    fun getResource(name: String): URL

    /**
     * リソースの <code>InputStream</code> を取得する
     * @param name リソース名
     * @return InputStream
     */
    fun getResourceAsStream(name: String): InputStream
}

/**
 * <code>Class<T></code> からリソースを取得する
 * @author funczz
 */
class ClassResourceReader<T>(

    /**
     * リソースの取得に用いるオブジェクト
     */
    private val clazz: Class<T>

) : ResourceReader {

    @Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
    override fun getResource(name: String): URL {
        return clazz.getResource(name)
    }

    @Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
    override fun getResourceAsStream(name: String): InputStream {
        return clazz.getResourceAsStream(name)
    }

}

/**
 * リソースを取得する
 * @param name リソース名
 * @return URL
 */
fun Any.getResource(name: String): URL =
    ClassResourceReader(this::class.java).getResource(name)

/**
 * リソースの <code>InputStream</code> を取得する
 * @param name リソース名
 * @return InputStream
 */
fun Any.getResourceAsStream(name: String): InputStream =
    ClassResourceReader(this::class.java).getResourceAsStream(name)

/**
 * <code>ClassLoader</code> からリソースを取得する
 * @author funczz
 */
class ClassLoaderResourceReader(

    /**
     * リソースの取得に用いるオブジェクト
     */
    private val classLoader: ClassLoader = ClassLoader.getSystemClassLoader()

) : ResourceReader {

    @Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
    override fun getResource(name: String): URL {
        return classLoader.getResource(name)
    }

    @Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
    override fun getResourceAsStream(name: String): InputStream {
        return classLoader.getResourceAsStream(name)
    }

}

/**
 * <code>FileSystem</code> からリソースを取得する
 * @author funczz
 */
open class FileSystemResourceReader(

    /**
     * リソースの取得に用いるオブジェクト
     */
    open val fileSystem: FileSystem = FileSystems.getDefault()

) : ResourceReader {

    override fun getResource(name: String): URL {
        return stringToPath(name).toUri().toURL()
    }

    override fun getResourceAsStream(name: String): InputStream {
        return stringToPath(name).toFile().inputStream()
    }

    private fun stringToPath(path: String): Path {
        return fileSystem.getPath(path).toAbsolutePath()
    }

}

/**
 * <code>URI</code> のパスからリソースを取得する
 * @author funczz
 */
open class URIResourceReader : ResourceReader {

    override fun getResource(name: String): URL {
        return stringToPath(name).toUri().toURL()
    }

    override fun getResourceAsStream(name: String): InputStream {
        return stringToPath(name).toFile().inputStream()
    }

    private fun stringToPath(path: String): Path {
        return File(URI(path).normalize()).toPath()
    }

}