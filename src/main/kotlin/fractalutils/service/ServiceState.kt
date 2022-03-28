package fractalutils.service

enum class ServiceState(val initialized: Boolean,
                        val shutDown: Boolean) {
    UNINITIALIZED(false, false),
    INITIALIZING(false, false),
    ACTIVE(true, false),
    SHUTTING_DOWN(true, false),
    SHUTDOWN(true, true),
}