package fractalutils.resource

import fractalutils.stream.readToString
import fractalutils.strings.prefix
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
    val resourceStream = getResourceAsStream(resourcePath)
    try {
        return resourceStream.readToString(charset) // Closes the stream
    } catch (e: IOException) {
        throw IOException("Could not read resource at '$resourcePath': " + e.message, e)
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
    val resourceStream = getResourceAsStream(resourcePath)
    try {
        return resourceStream.readBytes() // Does not close the stream
    } catch (e: IOException) {
        throw IOException("Could not read resource at '$resourcePath': " + e.message, e)
    } finally {
        resourceStream.close()
    }
}

/**
 * Opens the content of a resource included in the JAR as an InputStream.
 * It is the callers responsibility to close the returned stream when ready.
 *
 * @param resourcePath path to resource to read.
 * @param classLoaderAccessor can be used to pass in a java class object whose classloader is used to retrieve the resources.
 *        Unless modules or fancy live classloading is used the default should probably work fine.
 * @return stream with the resource.  The caller should close it when done.
 * @throws IOException if the resource was not found.
 */
@Throws(IOException::class)
fun getResourceAsStream(resourcePath: String, classLoaderAccessor: Class<Any> = ClassLoaderAccessor.javaClass): InputStream {
    val path = resourcePath.prefix("/", true).replace('\\', '/')
    try {
        return classLoaderAccessor.getResourceAsStream(path)
            ?: throw IOException("No resource found at location '$path'")
    } catch (e: IOException) {
        throw IOException("Could not access resource at '$path': " + e.message, e)
    }
}


// Used to access the classloader and the resources through it.
private object ClassLoaderAccessor