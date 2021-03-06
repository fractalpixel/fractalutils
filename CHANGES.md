## v3.0.1, 2022-03-30: Added Resource System.
Added a resource system for loading various resources, and implementations that use the file system and resources on 
the classpath.  This abstracts away the source of some loaded data.  It also allows writing custom resource systems,
e.g. for generated data or data loaded over the network.

The Resource files also provide a function for loading text resources and resolving simple #include
directives, which is useful e.g. for shader code that do not support includes natively.

## v3.0.2, 2022-04-05: Added support for querying the original source files for resources with includes.
Added a SourceLineMapping class for mapping from positions in a combined source to the original source and line in it.
An instance of SourceLineMapping can now be passed to Resource.readTextWithIncludes(), and will be filled with
the mappings from the result to the original sources and locations in them.  Useful for tracing compile error
messages and similar to their original locations.

## v3.0.3, 2022-04-05: Added memory and singleton resources.
Added MemResourceSystem and MemResource for storing resources in memory, providing a unified API to tree-structured
resource collections along with file and classpath based resources.
Added SingletonResource for storing single resources as a convenient way to pass data to APIs that take Resources as input.

## v3.0.4, 2022-04-11: Allow overriding ServiceBase.init()
Necessary e.g. to define custom documentation for it in interfaces that are implemented by classes that extend ServiceBase.
