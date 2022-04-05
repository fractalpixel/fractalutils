package fractalutils.resource

import java.io.IOException
import java.io.InputStream

/**
 * Simple utility class for cases when non-existing resources are needed.
 */
class NonExistingResource(override val path: String, override val resourceSystem: ResourceSystem): Resource {
    override val exists: Boolean get() = false
    override val isFile: Boolean get() = false
    override val isDirectory: Boolean get() = false

    override fun forEach(visitor: (Resource) -> Unit) {
        // Do nothing
    }

    override fun readStream(): InputStream {
        throw IOException("The resource $path does not exist")
    }
}