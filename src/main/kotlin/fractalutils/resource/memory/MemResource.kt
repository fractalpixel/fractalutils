package fractalutils.resource.memory

import fractalutils.resource.Resource
import java.io.IOException
import java.io.InputStream
import java.nio.charset.Charset

/**
 * A resource stored in memory.
 */
class MemResource(
    override val resourceSystem: MemResourceSystem,
    override val path: String): Resource {

    override val exists: Boolean get() = resourceSystem.getEntry(path) != null
    override val isFile: Boolean get() = resourceSystem.getEntry(path) is MemResourceSystem.File
    override val isDirectory: Boolean get() = resourceSystem.getEntry(path) is MemResourceSystem.Dir

    override fun forEach(visitor: (Resource) -> Unit) {
        val dir = resourceSystem.getEntry(path)
        if (dir is MemResourceSystem.Dir) {
            dir.entries.keys.forEach{
                visitor(MemResource(resourceSystem, path + Resource.DIRECTORY_SEPARATOR.toString() + it))
            }
        }
    }

    override fun readStream(): InputStream {
        return getFile().inputStream()
    }

    override fun readText(charset: Charset): String {
        return getFile().data.toString(charset)
    }

    override fun readBytes(): ByteArray {
        return getFile().data
    }

    private fun getFile(): MemResourceSystem.File {
        val entry = resourceSystem.getEntry(path)
        when (entry) {
            is MemResourceSystem.File -> return entry
            is MemResourceSystem.Dir -> throw IOException("The memory resource '$path' was a directory")
            else -> throw IOException("The memory resource '$path' was not found")
        }
    }
}