package fractalutils.resource

import fractalutils.stream.readToString
import java.io.IOException
import java.io.InputStream
import java.nio.charset.Charset

/**
 * Reads the content of a resource included in the JAR as a string.
 *
 * @param resourcePath path to resource to read
 * @return contents of the resource as text, using the specified [charset] (defaults to UTF 8).
 * @throws IOException if the resource was not found or could not be read.
 */
@Throws(IOException::class)
fun getResourceAsString(resourcePath: String, charset: Charset = Charsets.UTF_8): String {
    getResourceAsStream(resourcePath).use {
        return it.readToString(charset)
    }
}

/**
 * Reads the content of a resource included in the JAR as a byte array.
 *
 * @param resourcePath path to resource to read
 * @return contents of the resource as a byte array.
 * @throws IOException if the resource was not found or could not be read.
 */
@Throws(IOException::class)
fun getResourceAsBytes(resourcePath: String): ByteArray {
    getResourceAsStream(resourcePath).use {
        return it.readBytes()
    }
}

/**
 * Opens the content of a resource included in the JAR as an InputStream.
 * It is the callers responsibility to close the returned stream when ready.
 *
 * @param resourcePath path to resource to read.
 * @param classLoader the classloader to load resources with.
 *        Unless modules or fancy live classloading is used the default should probably work fine.
 * @return stream with the resource.  The caller should close it when done.
 * @throws IOException if the resource was not found.
 */
@Throws(IOException::class)
fun getResourceAsStream(resourcePath: String,
                        classLoader: ClassLoader = getDefaultClassLoader()): InputStream {
    val path = resourcePath.replace('\\', '/')
    try {
        return classLoader.getResourceAsStream(path)
            ?: throw IOException("No resource found at location '$path'")
    } catch (e: IOException) {
        throw IOException("Could not access resource at '$path': " + e.message, e)
    }
}

/**
 * Returns classloader.
 *
 * First looks for the context classloader from the current thread, if that is not specified looks
 * for a classloader for this library, if that is unspecified fetches the system classloader.
 */
fun getDefaultClassLoader(): ClassLoader {
    return getContextClassLoader() ?: getClassLoaderForClass(ClassLoaderAccessor.javaClass)
}

/**
 * Returns the classloader for the specified class if it has a custom one specified,
 * or the default system class loader if it doesn't have a classloader.
 */
fun getClassLoaderForClass(classToUse: Class<Any>): ClassLoader {
    return classToUse.classLoader ?: ClassLoader.getSystemClassLoader()
}

/**
 * Returns the classloader from the current thread, if specified.
 * Returns null if no such classloader is used.
 */
fun getContextClassLoader(): ClassLoader? {
    return Thread.currentThread().contextClassLoader
}

// Used to access the classloader and the resources through it.
private object ClassLoaderAccessor