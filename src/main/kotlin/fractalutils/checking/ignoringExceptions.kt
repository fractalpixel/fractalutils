package fractalutils.checking

import kotlin.reflect.KClass


/**
 * Run the specified block, silently ignoring the specified exception and returning the null for them instead,
 * otherwise returning the result of running the block.
 * @param ignoredException the exception to ignore.  Also all subclasses of this exception are ignored.
 * @param block code to run
 */
inline fun <T>ignoringException(ignoredException: KClass<out Throwable>, block: () -> T?): T? {
    return ignoringException(ignoredException, null, block)
}


/**
 * Run the specified block, silently ignoring the specified exception and returning the resultOnFail for them instead,
 * otherwise returning the result of running the block.
 * @param ignoredException the exception to ignore.  Also all subclasses of this exception are ignored.
 * @param resultOnFail result to return if the block throws an ignored exception.
 * @param block code to run
 */
inline fun <T>ignoringException(ignoredException: KClass<out Throwable>, resultOnFail: T, block: () -> T): T {
    return try {
        block()
    }
    catch (e: Throwable) {
        // Check if we can ignore this exception
        if (ignoredException.java.isAssignableFrom(e.javaClass)) return resultOnFail

        // We could not ignore that exception, rethrow it
        throw e
    }
}

/**
 * Run the specified block, silently ignoring the specified exceptions and returning the null for them instead,
 * otherwise returning the result of running the block.
 * @param ignoredExceptions the exceptions to ignore.  Also all subclasses of these exception are ignored.
 * @param block code to run
 */
inline fun <T>ignoringExceptions(ignoredExceptions: Collection<KClass<out Throwable>>, block: () -> T?): T? {
    return ignoringExceptions(ignoredExceptions, null, block)
}


/**
 * Run the specified block, silently ignoring the specified exceptions and returning the resultOnFail for them instead,
 * otherwise returning the result of running the block.
 * @param ignoredExceptions the exceptions to ignore.  Also all subclasses of these exception are ignored.
 * @param resultOnFail result to return if the block throws an ignored exception.
 * @param block code to run
 */
inline fun <T>ignoringExceptions(ignoredExceptions: Collection<KClass<out Throwable>>, resultOnFail: T, block: () -> T): T {
    return try {
        block()
    }
    catch (e: Throwable) {
        // Check if we can ignore this exception
        for (ignoredException in ignoredExceptions) {
            if (ignoredException.java.isAssignableFrom(e.javaClass)) return resultOnFail
        }

        // We could not ignore that exception, rethrow it
        throw e
    }
}
