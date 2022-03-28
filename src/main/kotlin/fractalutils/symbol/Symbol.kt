package fractalutils.symbol

import fractalutils.checking.Check
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.atomic.AtomicInteger


/**
 * A unique string type symbol, where each unique string is guaranteed to exist only once.
 * This way they can be identity compared instead of string compared.
 * Similar to the Symbol class in scala.
 *
 * NOTE: If using de-serialized Symbol instances, identity equals can not be used,
 * and normal equals should be used instead (it will still be just one int comparison instead of a string comparison though).
 *
 * Each Symbol is required to start with a letter or underscore, and contain letters, underscores, or numbers.
 *
 * Thread safe, the Symbols are unique across threads.
 */
class Symbol
/**
 * Creates a new instance of a symbol.
 * @param string name of the symbol.
 * *             Must not be null or an empty string.
 * *             Must start with a-z, _, or A-Z, and contain only a-z, A-Z, _, or 0-9.
 */
private constructor(val string: String) {

    @Transient private var uniqueId: Int = 0

    init {
        Check.strictIdentifier(string, "name")
    }

    override fun equals(other: Any?): Boolean {
        return if (other === this)
            true
        else if (other == null || other !is Symbol)
            false
        else {
            getUniqueId() == other.getUniqueId()
        }
    }

    override fun hashCode(): Int {
        return getUniqueId()
    }

    private fun getUniqueId(): Int {
        // Get unique id if we do not have one
        // We do not have one if this class was de-serialized.
        if (uniqueId == 0) {
            // Double check that the name is valid (in case serialized data tried to inject an invalid name)
            Check.strictIdentifier(string, "name")

            // Get existing symbol with same name if found
            var symbol: Symbol? = symbols[string]
            if (symbol == null) {
                // No existing symbol found

                // Get next free unique id
                uniqueId = nextFreeId.andIncrement

                // Try to add ourselves, or get already added symbol, if one was added from another thread.
                symbol = symbols.putIfAbsent(string, this)
            }

            if (symbol != null) {
                // Symbol existed, use unique id of existing symbol with same name
                // This breaks identity equality (need to use equals instead of === if using de-serialized Symbols),
                // but equals is still fast as it just compares ints.
                uniqueId = symbol.uniqueId
            }
        }

        return uniqueId
    }

    override fun toString(): String {
        return string
    }

    companion object {

        private val symbols = ConcurrentHashMap<String, Symbol>()
        private val nextFreeId = AtomicInteger(1)

        /**
         * @param name string to put in the symbol.
         * *             Must not be null or an empty string.
         * *             Must start with a-z, _, or A-Z, and contain only a-z, A-Z, _, or 0-9.
         * @return the symbol for the specified name.
         */
        operator fun get(name: String): Symbol {
            Check.strictIdentifier(name, "name")

            // Get symbol if found
            var symbol: Symbol? = symbols[name]
            if (symbol == null) {
                // No existing symbol with the same name found

                // Create new symbol with a new unique id
                val newSymbol = Symbol(name)
                newSymbol.uniqueId = nextFreeId.andIncrement

                // Try to add symbol, but if someone else already added one in between use that instead.
                symbol = symbols.putIfAbsent(name, newSymbol)
                if (symbol == null) {
                    symbol = newSymbol
                }
            }

            return symbol
        }
    }
}
