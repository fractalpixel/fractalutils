package fractalutils.resource.singleton

import fractalutils.resource.NonExistingResource
import fractalutils.resource.Resource
import fractalutils.resource.ResourceSystem
import java.nio.charset.Charset

/**
 * A resource system that only contains one resource, for cases where an API only accepts ResourceSystems,
 * and a single resource should be passed in.
 */
class SingletonResourceSystem(private val resource: SingletonResource): ResourceSystem {

    constructor(resourcePath: String, resourceData: ByteArray): this(SingletonResource(resourceData, resourcePath))
    constructor(resourcePath: String, resourceData: String, charset: Charset = Charsets.UTF_8): this(SingletonResource(resourceData, resourcePath, charset))

    override fun get(path: String): Resource {
        return if (path == resource.path) {
            // Ensure this resource system is set for the resource
            resource.singletonResourceSystem = this

            resource
        }
        else NonExistingResource(path, this)
    }

}