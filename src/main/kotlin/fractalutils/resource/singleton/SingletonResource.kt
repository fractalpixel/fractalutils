package fractalutils.resource.singleton

import fractalutils.resource.Resource
import fractalutils.resource.ResourceSystem
import java.io.ByteArrayInputStream
import java.io.InputStream
import java.nio.charset.Charset

/**
 * A resource that directly encapsulates data itself, used to pass data to an API that only accepts Resources.
 */
class SingletonResource(val data: ByteArray, override val path: String = "", internal var singletonResourceSystem: SingletonResourceSystem? = null): Resource {

    constructor(data: String, path: String = "", charset: Charset = Charsets.UTF_8, singletonResourceSystem: SingletonResourceSystem? = null): this(data.toByteArray(charset), path, singletonResourceSystem)

    override val resourceSystem: ResourceSystem get() {
        var r = singletonResourceSystem
        if (r == null) {
            // Create a SingletonResourceSystem with this resource if a resource system is queried for, and we don't already have one.
            r = SingletonResourceSystem(this)
            singletonResourceSystem = r
        }
        return r
    }

    override val exists: Boolean get() = true
    override val isFile: Boolean get() = true
    override val isDirectory: Boolean get() = false

    override fun forEach(visitor: (Resource) -> Unit) {
        // Do nothing
    }

    override fun readStream(): InputStream {
        return ByteArrayInputStream(data)
    }

    override fun readText(charset: Charset): String {
        return data.toString(charset)
    }

    override fun readBytes(): ByteArray {
        return data
    }
}