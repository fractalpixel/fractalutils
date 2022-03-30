package fractalutils.resource.classpath

import fractalutils.resource.Resource
import fractalutils.resource.ResourceSystem
import fractalutils.resource.ResourceSystemBase
import fractalutils.resource.getDefaultClassLoader
import fractalutils.strings.suffix

/**
 * [ResourceSystem] implementation that loads resources embedded in the projects Jar files,
 * using the classpath resource loading functionality.
 *
 * Does not support detecting directories or iterating directories.
 *
 * @param rootPath all file paths in this [ResourceSystem] are considered to be relative to this root path,
 * if specified.  Defaults to empty string.
 * @param classLoader the classloader that is used to load the resources from the projects Jar file.
 * Unless modules or custom classloaders are used, this can be left as the default.
 */
class JarResourceSystem(rootPath: String = "",
                        val classLoader: ClassLoader = getDefaultClassLoader()):
    ResourceSystemBase(JAR_PACKAGE_SEPARATOR) {

    /**
     * Path to prefix any resource accesses with.
     */
    private val rootPath: String = rootPath.trim().let {
        // Make sure the root path ends with / if present
        if (it.isEmpty()) ""
        else dirSeparatorsToUnderlyingSystem(it.removePrefix("/")).suffix(JAR_PACKAGE_SEPARATOR.toString(), true)
    }

    override fun get(path: String): Resource {
        return JarResource( dirSeparatorsToUnderlyingSystem(path), this)
    }

    /**
     * The specified resource path as a jar resource path.
     */
    fun getJarPath(path: String): String {
        return rootPath + dirSeparatorsToUnderlyingSystem(path).removePrefix("/")
    }

    companion object {
        // Directory separator used with classloaders
        const val JAR_PACKAGE_SEPARATOR = '/'
    }
}

