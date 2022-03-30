package fractalutils.resource.file

import fractalutils.resource.ResourceSystem
import fractalutils.resource.ResourceSystemBase
import java.io.File

/**
 * A [ResourceSystem] backed by the file system, or a path in the file system.
 *
 * @param rootDirectory all file paths in this [ResourceSystem] are considered to be relative to this root folder,
 * if specified.  Defaults to null, which uses no parent (root folder default to current working directory).
 *
 * Note that it's possible to use path navigation such as ".." to access files outside the root folder,
 * so it should not be assumed to be securely sandboxed.
 */
class FileResourceSystem(val rootDirectory: File? = null): ResourceSystemBase(File.separatorChar) {

    override fun get(path: String): FileResource {
        // Convert directory separators to system settings
        val systemPath = dirSeparatorsToUnderlyingSystem(path)

        // Construct file using the root folder specified, if any
        val file = if (rootDirectory != null) File(rootDirectory, systemPath) else File(systemPath)

        // Return file encapsulated as a resource
        return FileResource(file, this)
    }

}