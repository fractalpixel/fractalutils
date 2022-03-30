package fractalutils.resource.classpath

import fractalutils.resource.Resource
import fractalutils.resource.getResourceAsStream
import java.io.InputStream

/**
 * A Resource that is loaded from a jar.
 * Does not support detecting directories or iterating directories.
 *
 * NOTE: To implement working directory traversal support for classpath resources is not trivial.
 * if that is desired, a library such as https://github.com/classgraph/classgraph could be used,
 * but it was not implemented here (at least so far) to keep the number of dependencies down,
 * and due to complexity.
 */
class JarResource(override val path: String,
                  override val resourceSystem: JarResourceSystem): Resource {

    private val jarPath: String get() = resourceSystem.getJarPath(path)

    override val exists: Boolean get() = isFile // || isDirectory
    override val isFile: Boolean get() = resourceSystem.classLoader.getResource(jarPath) != null
    override val isDirectory: Boolean get() = throw UnsupportedOperationException("JarResources can't detect directories")

    override fun forEach(visitor: (Resource) -> Unit) {
        throw UnsupportedOperationException("JarResources can't list directory contents")
    }

    override fun readStream(): InputStream {
        return getResourceAsStream(jarPath, resourceSystem.classLoader)
    }


}