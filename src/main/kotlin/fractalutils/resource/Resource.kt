package fractalutils.resource

import fractalutils.stream.readToString
import fractalutils.strings.suffix
import java.io.IOException
import java.io.InputStream
import java.nio.charset.Charset
import java.util.*
import kotlin.collections.ArrayList

/**
 * Represents a path to a resource in some [ResourceSystem].
 * Similar to [java.io.File].
 *
 * Can represent either an individual resource, or a directory with other resources,
 * or point to a place that has no resource.
 */
interface Resource {

    /**
     * The resource system that this resource is contained in.
     */
    val resourceSystem: ResourceSystem

    /**
     * Full resource path from the root of the [ResourceSystem].
     * Directories are separated with '/' characters.
     */
    val path: String

    /**
     * Just the name of the resource, without the directories it is located in.
     * Includes any filename extension.
     */
    val name: String get() = path.substringAfterLast(DIRECTORY_SEPARATOR)

    /**
     * The path of the directory that this file is located in, without any trailing '/'.
     * Empty string if there is no directory.
     */
    val parentPath: String get() = path.substringBeforeLast(DIRECTORY_SEPARATOR, "")

    /**
     * The last part of a filename after the last dot.  If the name contains no dots, returns an empty string.
     */
    val extension: String get() = path.substringAfterLast(EXTENSION_SEPARATOR, "")

    /**
     * The name of the resource, with the last dot and any trailing extension removed.
     */
    val nameWithoutExtension: String get() = name.substringBeforeLast(EXTENSION_SEPARATOR)

    /**
     * True if this resource exists.
     */
    val exists: Boolean

    /**
     * True if this resource path refers to a concrete existing file / data object, and not a directory.
     */
    val isFile: Boolean

    /**
     * True if this resource path refers to an existing directory.
     */
    val isDirectory: Boolean

    /**
     * A resource representing the parent directory of this resource, or null if this resource has no
     * parent directory in the [ResourceSystem] it is located in.
     */
    val parent: Resource? get() = parentPath.let {
       if (it.isNotEmpty()) {
           resourceSystem[it]
       }
       else null
    }

    /**
     * Calls the provided code block for each child of this resource, if it is a directory.
     * If this resource doesn't exist or isn't a directory, does nothing.
     *
     * @param visitor called for each child resource.
     */
    fun forEach(visitor: (Resource) -> Unit)

    /**
     * Recursively visits this resource or children of it.
     *
     * Note that unlike forEach, this also visits the resource it was called on if it is a file.
     *
     * @param directoryFilter recursively iterate directories matching this.  Defaults to visiting all directories.
     * @param fileVisitor called for each matching file resource.
     */
    fun forEachRecursively(directoryFilter: (Resource) -> Boolean = {true},
                           fileVisitor: (Resource) -> Unit) {

        if (isFile) {
            fileVisitor(this)
        }
        else if (isDirectory && directoryFilter(this)) {
            forEach {
                it.forEachRecursively(directoryFilter, fileVisitor)
            }
        }
    }

    /**
     * Return children of this resource, if it is a directory.
     * If this resource doesn't exist or isn't a directory, returns an empty collection.
     *
     * @param filter a filter to check if the resource should be returned.  Defaults to returning all resources.
     */
    fun list(filter: (Resource) -> Boolean = {true}): List<Resource> {
        return if (isDirectory) {
            val result = ArrayList<Resource>()
            forEach {
                if (filter(it)) {
                    result.add(it)
                }
            }
            result
        }
        else Collections.emptyList<Resource>()
    }

    /**
     * Recursively returns this resource or children of it.
     *
     * Note that unlike list, this also lists the resource it was called on if it is a file matching the fileFilter.
     *
     * @param fileFilter return files matching this.  Default to returning all files.
     * @param directoryFilter recursively iterate directories matching this.  Defaults to visiting all directories.
     * @param result list to add the results to, returned by this function.  By default, creates a new ArrayList.
     */
    fun listRecursively(fileFilter: (Resource) -> Boolean = {true},
                        directoryFilter: (Resource) -> Boolean = {true},
                        result: MutableList<Resource> = ArrayList()): List<Resource> {

        forEachRecursively(directoryFilter) {
            if (it.isFile && fileFilter(it)) {
                result.add(it)
            }
        }

        return result
    }

    /**
     * Load resource as text, using the specified character set (defaults to UTF8).
     * @throws IOException if the resource was not found or could not be read.
     */
    @Throws(IOException::class)
    fun readText(charset: Charset = Charsets.UTF_8): String {
        // Default implementation delegating to readStream():
        readStream().use {
            return it.readToString(charset)
        }
    }

    /**
     * Load the resource as an array of bytes.
     * @throws IOException if the resource was not found or could not be read.
     */
    @Throws(IOException::class)
    fun readBytes(): ByteArray {
        // Default implementation delegating to readStream():
        readStream().use {
            return it.readBytes()
        }
    }

    /**
     * Open an input stream to the resource.
     *
     * NOTE: It is the responsibility of the caller to close the returned stream when done.
     * @throws IOException if the resource was not found or could not be read.
     */
    @Throws(IOException::class)
    fun readStream(): InputStream



    /**
     * Loads the specified resource as text, and replaces all "#include otherResourceName\n" instances with the specified resources.
     * Only includes each resource once.
     *
     * The #include must be at the start of a line (whitespace may precede it), and end in a newline.
     * The included resource name is trimmed of whitespace before used.
     *
     * Only works for text files.
     *
     * @param resourcePath a path to prefix to any included resource names, or null if includes are from the root path.
     * @param includeDirective a string indicating that the rest of the line is a filename to be included.
     * Defaults to "#include".
     * @param ignoreDirectiveCase if true, the exact case of the directive is ignored (matches both upper and lower cases).
     * The actual included files are tested with exact case though. Defaults to true.
     * @param charSet the character set that the input text files use.  Defaults to UTF8.
     * @param sourceMappingOut an optional [SourceLineMapping] that the source names and locations for each line in
     *        the resulting text are written to.  Can be used e.g. to look up locations of error messages.
     * @return The resulting text, with included result from other resources.
     * @throws IOException if the resource or an included resource was not found or could not be read.
     */
    fun readTextWithIncludes(resourcePath: String? = null,
                             includeDirective: String = "#include",
                             ignoreDirectiveCase: Boolean = true,
                             sourceMappingOut: SourceLineMapping<String>? = null,
                             charSet: Charset = Charsets.UTF_8): String {

        val alreadyIncluded = HashSet<String>()
        val resultBuilder = StringBuilder()
        sourceMappingOut?.clear()

        // Load resource
        val resourceText: String = readText(charSet)

        doLoadResourceParsingIncludes(
            this,
            resourceText,
            resourcePath?.suffix(DIRECTORY_SEPARATOR.toString(), true) ?: "",
            includeDirective,
            ignoreDirectiveCase,
            resultBuilder,
            alreadyIncluded,
            sourceMappingOut,
            charSet)

        return resultBuilder.toString()
    }

    private fun doLoadResourceParsingIncludes(resource: Resource,
                                              resourceText: String,
                                              resourcePath: String,
                                              includeDirective: String,
                                              ignoreDirectiveCase: Boolean,
                                              resultBuilder: StringBuilder,
                                              alreadyIncluded: HashSet<String>,
                                              sourceMappingOut: SourceLineMapping<String>?,
                                              charSet: Charset) {

        // Do not include things multiple times
        alreadyIncluded.add(resource.path)

        // Loop lines
        resourceText.lines().forEachIndexed() { lineNum: Int, line: String ->
            // Check for includes
            if (line.trim().startsWith(includeDirective, ignoreDirectiveCase)) {
                // Include line, include the included file if not already included
                val includePath = resourcePath + line.trim().substring(includeDirective.length).trim()
                if (!alreadyIncluded.contains(includePath)) {
                    val includedResource = resourceSystem[includePath]
                    val includedResourceText: String = try {
                        // Load resource
                        includedResource.readText(charSet)
                    } catch (e: Exception) {
                        // Try to be clear about errors
                        throw IOException("Could not load resource '$includePath' included from line $lineNum in resource '${resource.path}': $e", e)
                    }

                    // Add the included file to the builder, and check for any additional includes in it.
                    doLoadResourceParsingIncludes(includedResource, includedResourceText, resourcePath, includeDirective, ignoreDirectiveCase, resultBuilder, alreadyIncluded, sourceMappingOut, charSet)
                }
            } else {
                // Normal line, include in result
                resultBuilder.append(line).append("\n")

                // Build mapping
                sourceMappingOut?.appendLineMapping(resource.path, lineNum + 1) // Lines start with 1
            }
        }
    }



    companion object {
        /**
         * The directory separator character used by resources.
         */
        const val DIRECTORY_SEPARATOR = '/'

        /**
         * The separator between a resource name and its extension.
         */
        const val EXTENSION_SEPARATOR = '.'
    }
}

