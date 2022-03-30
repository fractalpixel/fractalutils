## v3.0.1, 2022-04-30: Added Resource System.
Added a resource system for loading various resources, and implementations that use the file system and resources on 
the classpath.  This abstracts away the source of some loaded data.  It also allows writing custom resource systems,
e.g. for generated data or data loaded over the network.

The Resource files also provide a function for loading text resources and resolving simple #include
directives, which is useful e.g. for shader code that do not support includes natively.