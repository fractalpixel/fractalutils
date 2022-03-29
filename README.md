[![Build Status](https://github.com/fractalpixel/fractalutils/actions/workflows/run-tests-on-push.yml/badge.svg)](https://github.com/fractalpixel/fractalutils/actions/workflows/run-tests-on-push.yml)
[![Release](https://jitpack.io/v/fractalpixel/fractalutils.svg)](https://jitpack.io/#fractalpixel/fractalutils)

# FractalUtils

A collection of utility functions and classes written in Kotlin.  
There is some emphasis on utilities useful for games (Geometry, Random, Time, Updating, etc).

Has unit tests for some, but not all utilities. 

May have breaking changes introduced between major versions.

## License

Licensed under the GNU LGPL v3 license.


## Including

A maven / gradle package of this library can be downloaded from [jitpack](https://jitpack.io/#fractalpixel/fractalutils):

Add jitpack.io as a repository (gradle):
<pre>
repositories {
    ...
    maven { url 'https://jitpack.io' }
}
</pre>

Add dependency:
<pre>
dependencies {
    implementation 'com.github.fractalpixel:fractalutils:TAG'
}
</pre>

Where TAG is the release to use (e.g. "v3.0.0", see tags in the sidebar or 
[![Release](https://jitpack.io/v/fractalpixel/fractalutils.svg)](https://jitpack.io/#fractalpixel/fractalutils) 
for latest release version).


## Usage
Most classes and function have kotlin docs. 

The utilities are grouped into packages with related functionality.  The main packages are:

### Checking
Contains the Check utility class, for checking function parameters and invariants, 
and throwing descriptive exceptions when the checks fail.  E.g. Check.positiveOrZero(myParam, "myParam").

### Collections
Collection related utility functions and classes.

The RingBuffer is a fixed length buffer that can have values added or removed either to/from the beginning or end,
dropping values when the buffer becomes full.  It has fast implementations for various primitive types as well as a
generic implementation.

The WeightedMap is a map where each value has some numerical weights, values can be randomly picked from it
with a probability relative to their weight in the map.

### File
Functions for things like saving a file reliably (using a temporary file and verifying it before replacing 
the previous version of a file), and recursively iterating files in a directory.

### Geometry
Contains 2 and 3 position double and integer vectors, as well as volumes and rectangles.
Has immutable and mutable variants of each, and a common interface which is easy to implement.

Not as fast as vector classes could be, as inlining can't be used due to the polymorphism of immutable and mutable variants.

As opposed to most 3D library vectors, these are Double typed, so can work with larger ranges and smaller
cumulative changes without errors.

### Interpolation
Contains a set of easing classes for creating various non-linear interpolations, a gradient class, as well as 
an interface for mixing functions / mixable values, that can be used with the easings or gradient to interpolate 
arbitrary user defined types or classes provided by other libraries.

### Logic
Contains a few utility extension functions such as <boolean expression>.ifElse(valueIfTrue, valueIfFalse) and similar.

### Math
A large collection of utility functions and extension functions for doing common math.

Perhaps most notably the mix and map functions for mapping a value in a numerical range to another range 
(optionally applying an easing at the same time).

Also has various clamping, wrapping and rounding methods.

### Random
An interface for pseudorandom number generators with a lot of utility functions (e.g. getting a random entry from a
collection), as well as a few implementations (XoroShiro etc).  Motivated by the notable artifacts especially in
low bits that are visible in the default Java Random implementation.  XoroShiro and similar functions are also 
quite fast (but should of course not be used for generating secure random numbers).

Also includes an interface for hashing functions and an implementation.

### Resource
Utility for loading data from java resource files included in Jars.

### Service
Interface and base classes for components of an application that are initialized at application start and shutdown
at application end, and that may need to access other such components of the application.

Allows encapsulating various functionality of an application into Services in a structured way, with some
lifecycle and access support.

The main class of an application could extend or contain a DefaultServiceProvider, to which other services
are added with addService().  Call init() to initialize all added services, and shutdown() to shut them down.
getService(serviceType) returns the service with the specified type (allowing access to services by using
interfaces if desired, further decoupling different services from each other).

### Stream
Stream related utility functions.

### Strings
A large set of utility and extension functions for working with strings, e.g. escaping, prefixing, etc.

Also contains an interface CodeBuilding for classes that can build some kind of code representation (usually 
e.g. an abstract syntax tree or nested nodes).  Has utilities for handling indent and such.

### Symbol
Symbol is a utility class that represents the concept of a unique, named string identifier.
Attempts to make comparing Symbols efficient (O(1)) (by keeping integer ids internally and using them for equality checks).
Symbols can be useful e.g. as object identifiers and hashtable keys. 
Symbol names must conform to the Java identifier format.

### Thread
Thread related utilities, e.g. threadLocal, which is a utility function for creating thread local object instances
in a more streamlined way than the standard Java syntax.

### Time
Interface and class for keeping track of time that advances in steps, e.g. in a simulation or game.  
Contains utility functions for getting total elapsed seconds, or seconds in the last time step.
Has various implementations, either where the time is manually advanced, or where the time reflects the system time.

TimeUtils.kt also has some utility functions for working with calendar time and timestamps 
(although using a dedicated library or the newer date API in Java for that purpose is recommended instead).

### Updating
Contains the Updating interface, for something that can be updated and is passed a Time with the current time.
Also has various update strategies, e.g. for handling updates that simulate a fixed amount of time.
Useful for game logic code or simulations.


## Reporting Issues

Please report any bugs or feature requests to the issue tracker at:
https://github.com/fractalpixel/fractalutils/issues

Pull requests for fixes are welcome too.
