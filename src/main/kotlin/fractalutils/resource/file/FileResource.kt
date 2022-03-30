package fractalutils.resource.file

import fractalutils.resource.Resource
import java.io.File
import java.io.InputStream
import java.nio.charset.Charset

/**
 * A [Resource] that represents a filesystem file.
 */
class FileResource(val file: File,
                   override val resourceSystem: FileResourceSystem): Resource {

    override val path: String get() = resourceSystem.dirSeparatorsToResourceSystem(file.path)
    override val exists: Boolean get() = file.exists()
    override val isFile: Boolean get() = file.isFile
    override val isDirectory: Boolean get() = file.isDirectory

    override fun forEach(visitor: (Resource) -> Unit) {
        file.listFiles()?.forEach {
            val fileResource = FileResource(it, resourceSystem)
            visitor(fileResource)
        }
    }

    override fun readText(charset: Charset): String {
        return file.readText(charset)
    }

    override fun readBytes(): ByteArray {
        return file.readBytes()
    }

    override fun readStream(): InputStream {
        return file.inputStream()
    }
}