package fractalutils.stream

import java.io.BufferedReader
import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader
import java.nio.charset.Charset


/**
 * Returns contents of the stream as text, using the specified [charset] (defaults to UTF 8).
 * Closes the stream whether the reading succeeds or not.
 *
 * @throws IOException if there was some problem.
 */
fun InputStream.readToString(charset: Charset = Charsets.UTF_8): String {
    BufferedReader(InputStreamReader(this, charset)).use {
        return it.readText()
    }
}