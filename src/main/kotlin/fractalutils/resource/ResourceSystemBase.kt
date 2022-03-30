package fractalutils.resource

/**
 * Provides convenience functions to ResourceSystem implementations.
 */
abstract class ResourceSystemBase(private val underlyingPathSeparator: Char = Resource.DIRECTORY_SEPARATOR): ResourceSystem {

    /**
     * Convert all path separators in the path from the resource system standard to the underlying system standard.
     */
    fun dirSeparatorsToUnderlyingSystem(path: String): String {
        return if (underlyingPathSeparator != Resource.DIRECTORY_SEPARATOR)
            path.replace(Resource.DIRECTORY_SEPARATOR, underlyingPathSeparator)
        else path
    }

    /**
     * Convert all path separators in the path from the underlying system standard to the resource system standard.
     */
    fun dirSeparatorsToResourceSystem(path: String): String {
        return if (underlyingPathSeparator != Resource.DIRECTORY_SEPARATOR)
            path.replace(underlyingPathSeparator, Resource.DIRECTORY_SEPARATOR)
        else path
    }

}