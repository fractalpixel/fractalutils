package fractalutils.file

import java.util.*
import java.nio.charset.Charset
import java.io.*


/**
 * Visit files matching the file filter under this directory and subdirectories matching the directory filter.
 * @param visitor method called for each matching file.  Takes the file and the parent path (as a list of directories) as parameters.
 */
fun File.recursivelyIterateFiles(
    visitor: (file: File, parentPath: List<File>) -> Unit,
    fileFilter: (File) -> Boolean,
    directoryFilter: (File) -> Boolean = { true },
    parentPath: List<File> = emptyList(),
    includeThisDirectoryInPath: Boolean = false
) {

    if (this.isFile && fileFilter(this)) visitor(this, parentPath)
    else if (this.isDirectory && directoryFilter(this)) {
        val path = ArrayList(parentPath)
        if (includeThisDirectoryInPath) path.add(this)
        for (file in this.listFiles() ?: emptyArray()) {
            file.recursivelyIterateFiles(visitor, fileFilter, directoryFilter, path, true)
        }
    }
}


/**
 * Lists files matching the file filter under this directory and subdirectories matching the directory filter.
 * @return list with pairs of subdirectory paths and files.  For files directly under this path the subdirectory path will be empty.
 */
fun File.recursivelyListFiles(
    fileFilter: (File) -> Boolean,
    directoryFilter: (File) -> Boolean = { true },
    outputList: MutableList<Pair<List<File>, File>> = java.util.ArrayList(),
    parentPath: List<File> = emptyList(),
    includeThisDirectoryInPath: Boolean = false
): List<Pair<List<File>, File>> {

    if (this.isFile && fileFilter(this)) outputList.add(Pair(parentPath, this))
    else if (this.isDirectory && directoryFilter(this)) {
        val path = ArrayList(parentPath)
        if (includeThisDirectoryInPath) path.add(this)
        for (file in this.listFiles() ?: emptyArray()) {
            file.recursivelyListFiles(fileFilter, directoryFilter, outputList, path, true)
        }
    }

    return outputList
}

/**
 * Creates a map with file paths being converted to keys, and files being converted to values.
 * @param fileFilter takes the path of the file, plus the file itself, and returns a key to use in the map for the value generated for the file.
 */
fun <K, V> File.recursivelyMapFiles(
    fileFilter: (File) -> Boolean,
    directoryFilter: (File) -> Boolean = { true },
    keyConverter: (List<File>) -> K,
    valueConverter: (File) -> V
): MutableMap<K, V> {

    val map = LinkedHashMap<K, V>()

    val files = this.recursivelyListFiles(fileFilter, directoryFilter)
    for ((path, file) in files) {
        val p = ArrayList(path)
        p.add(file)
        val key = keyConverter(p)
        val value = valueConverter(file)
        map[key] = value
    }

    return map
}



/**
 * Saves the given text to this file, using a temp file as a temporary initial storage.
 * Checks that the text written to the file matches with the text in memory before deleting the original file
 * and replacing it with the temporary file.
 *
 * @param text text to save
 * @param tempFile temporary file to first save data to.  If not specified, a tempFile with the same name as the
 * file except ".temp" appended will be used (or .temp and a number, if there is already a file ending in .temp).
 * @param charset the character encoding to use, defaults to UTF 8.
 * @throws IOException if there was some problem at any step when saving the data or verifying the saved data.
 */
@Throws(IOException::class)
fun File.saveAndCheck(text: String,
                      tempFile: File = this.temporaryFileName(),
                      charset: Charset = Charsets.UTF_8) {
    this.saveAndCheck(text.toByteArray(charset), tempFile)
}


/**
 * Saves the data to this file, using a temp file as a temporary initial storage.
 * Checks that the data written to the file matches with the data in memory before deleting the original file
 * and replacing it with the temporary file.
 *
 * @param data data to save
 * @param tempFile temporary file to first save data to.  If not specified, a tempFile with the same name as the
 * file except ".temp" appended will be used (or .temp and a number, if there is already a file ending in .temp).
 * @throws IOException if there was some problem at any step when saving the data or verifying the saved data.
 */
@Throws(IOException::class)
fun File.saveAndCheck(data: ByteArray, tempFile: File = this.temporaryFileName()) {

    // Save to temp file
    BufferedOutputStream(FileOutputStream(tempFile, false)).use {
        it.write(data)
        it.flush()
    }

    // Sanity check (e.g. if file system just pretends to write things)
    check(tempFile.exists()) { "The temp file ($tempFile) we just saved the stored object to does not exist anymore!" }

    // Check that the written file matches the data
    val inputStream = BufferedInputStream(FileInputStream(tempFile))

    // Load data written to the temp file
    val writtenFileData: ByteArray = inputStream.use { it.readBytes() }

    // Compare size of data to save and data read from temp file
    ioCheck(writtenFileData.size == data.size) {
        "The contents of the temp file ($tempFile) does not match the data written there! (different length)"
    }

    // Compare each byte
    for (i in data.indices) {
        ioCheck(data[i] == writtenFileData[i]) {
            "The contents of the temp file ($tempFile) does not match the data written there! (different data at byte $i)"
        }
    }

    // Delete the real file, if it exists (it might not if we haven't saved anything to the file previously)
    ioCheck(!exists() || delete()) {
        "Could not delete the storage file ($this) so that we could replace it with the temporary file ($tempFile) with new data."
    }

    // Replace real file with temp file
    ioCheck(tempFile.renameTo(this)) {
        "Could not rename the temporary storage file ($tempFile) to the real storage file ($this).  " +
         "$tempFile now contains the only copy of the data, move it to another name manually to avoid it getting overwritten!"
    }
}


/**
 * Create a temporary file name based on the specified file name but with [postfix] appended (".temp" by default).
 * If the temporary file name already exists, a new one is created by appending a number to the file name.
 * If more than [maxTries] file names were tried, throws IOException.
 */
fun File.temporaryFileName(postfix: String = ".temp", maxTries: Int = 1000): File {
    require(maxTries > 0) { "Should at least allow one try, but maxTries was $maxTries" }

    // Try with just postfix
    var tempFileName: File = File(this.path + postfix)
    if (!tempFileName.exists()) return tempFileName
    else {
        // Append numbers after postfix if they already exist
        for (i in 2..maxTries) {
            tempFileName = File(this.path + "$postfix$i")
            if (!tempFileName.exists()) return tempFileName
        }
    }

    // Someone is not deleting temporary files..
    throw IOException("Tried to find a temporary file name based on $this, but gave up after all temporary file names up to $tempFileName were in use.")
}


/**
 * Throws an [IOException] with the result of calling [lazyMessage] if the [value] is false.
 */
inline fun ioCheck(value: Boolean, lazyMessage: () -> Any): Unit {
    if (!value) {
        val message = lazyMessage()
        throw IOException(message.toString())
    }
}


