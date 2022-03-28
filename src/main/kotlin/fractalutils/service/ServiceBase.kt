package fractalutils.service

/**
 * Common functionality for services
 */
abstract class ServiceBase: Service {

    override var status: ServiceState = ServiceState.UNINITIALIZED
        protected set

    final override fun init() {
        init(null)
    }

    override fun init(serviceProvider: ServiceProvider?) {
        if (status != ServiceState.UNINITIALIZED) throw IllegalStateException("The $name service has already been initialized, can not initialize again!")
        status = ServiceState.INITIALIZING

        doInit(serviceProvider)

        status = ServiceState.ACTIVE
    }

    override fun shutdown() {
        if (status != ServiceState.ACTIVE) throw IllegalStateException("The $name service is not active, can not shut it down!")
        status = ServiceState.SHUTTING_DOWN

        doShutdown()

        status = ServiceState.SHUTDOWN
    }

    protected abstract fun doInit(serviceProvider: ServiceProvider?)
    protected abstract fun doShutdown()

    override fun toString(): String {
        return "$name [$status]"
    }
}