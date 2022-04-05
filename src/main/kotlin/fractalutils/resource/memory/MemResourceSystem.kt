package fractalutils.resource.memory

import fractalutils.resource.Resource
import fractalutils.resource.ResourceSystemBase
import java.io.ByteArrayInputStream
import java.io.InputStream
import java.nio.charset.Charset

/**
 * A resource system that stores resources in memory.
 */
class MemResourceSystem(): ResourceSystemBase() {

    private val root = Dir("")

    override fun get(path: String): MemResource {
        return MemResource(this, path)
    }

    /**
     * Store the data at the specified path.
     * @param path a path to the filename, using [Resource.EXTENSION_SEPARATOR] to separate directories.
     * @param data to store as text
     * @param charset the character set used to encode the stored data, defaults to UTF8
     */
    fun setData(path: String, data: String, charset: Charset = Charsets.UTF_8)  {
        setData(path, data.toByteArray(charset))
    }

    /**
     * Store the data at the specified path.
     * @param path a path to the filename, using [Resource.DIRECTORY_SEPARATOR] to separate directories.
     * @param data to store
     */
    fun setData(path: String, data: ByteArray)  {
        // Preceding / character is ignored.
        val dirSep = Resource.DIRECTORY_SEPARATOR.toString()
        val p = path.removePrefix(dirSep)

        val dir = createDirTo(p)

        val fileName = p.substringAfterLast(dirSep)
        dir.addFile(fileName, data)
    }

    internal fun getEntry(path: String, parentDir: Dir = root): Entry? {
        // Preceding / character is ignored.
        val dirSep = Resource.DIRECTORY_SEPARATOR.toString()
        val p = path.removePrefix(dirSep).removeSuffix(dirSep)

        return if (!p.contains(dirSep)) {
            parentDir.get(p)
        } else {
            // Get first directory, then recurse
            val firstDir = path.substringBefore(dirSep)
            val rest = path.substringAfter(dirSep)
            val dir = parentDir.get(firstDir)

            if (dir is Dir) getEntry(rest, dir) // Recurse
            else null // Directory not found, or was file
        }
    }

    /**
     * Creates parent directories for the specified file as needed.
     */
    internal fun createDirTo(path: String, currentDir: Dir = root): Dir {
        val dirSep = Resource.DIRECTORY_SEPARATOR.toString()
        return if (!path.contains(dirSep)) {
            // At the last file, or the root directory
            currentDir
        } else {
            // Create or get first directory, then recurse
            val firstDir = path.substringBefore(dirSep)
            val rest = path.substringAfter(dirSep)
            val dir = currentDir.getOrCreateSubDir(firstDir)
            createDirTo(rest, dir)
        }
    }

    internal interface Entry {
        val name: String
    }

    internal class Dir(override val name: String): Entry {
        val entries = LinkedHashMap<String, Entry>()

        fun get(name: String): Entry? = entries[name]

        /**
         * Returns subdirectory with the specified name, creating a new one if necessary.
         * Replaces any file with the specified name with a directory
         */
        fun getOrCreateSubDir(name: String): Dir {
            val entry = entries[name]
            return if (entry is Dir) entry // Return existing dir
            else {
                // Replace existing null or file
                val newDir = Dir(name)
                entries[name] = newDir
                newDir
            }
        }

        fun addFile(name: String, data: ByteArray): File {
            val f = File(name, data)
            entries[name] = f
            return f
        }
    }

    internal class File(override val name: String, val data: ByteArray): Entry {
        fun inputStream(): InputStream = ByteArrayInputStream(data)

    }

}