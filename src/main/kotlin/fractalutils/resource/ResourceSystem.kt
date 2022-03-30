package fractalutils.resource

/**
 * A tree like structure where named resources can be loaded from.
 * Abstracts the filesystem and classpath resources, providing a unified interface for both.
 * Can also be implemented for custom resource systems.
 */
interface ResourceSystem {

    /**
     * @return a [Resource] that represents the specified path from the root of this resource system.
     */
    operator fun get(path: String): Resource

}