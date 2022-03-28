package fractalutils.thread


/**
 * Wrapper for creating a ThreadLocal instance using the specified initialization block.
 */
fun <T>threadLocal(initialValueFunction: () -> T): ThreadLocal<T> {
    return object: ThreadLocal<T>() {
        override fun initialValue(): T {
            return initialValueFunction()
        }
    }
}