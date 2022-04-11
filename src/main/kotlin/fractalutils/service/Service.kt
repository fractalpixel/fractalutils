package fractalutils.service

import fractalutils.strings.toSpaceSeparated


/**
 * Something that provides initialization and shutdown functions.
 * The service has a distinct lifecycle, uninitialized -> initializing -> active -> shutting down -> shut down.
 */
interface Service {

    /**
     * Name of this service.
     */
    val name: String get() = this.javaClass.simpleName.toSpaceSeparated()

    /**
     * Current state of the service
     */
    val status: ServiceState

    /**
     * @return true if the Service is active (initialized but not yet disposed).
     */
    val active: Boolean get() = status == ServiceState.ACTIVE

    /**
     * @return true if the service has ever been initialized (it might have been shutdown as well though).
     */
    val initialized: Boolean get() = status.initialized

    /**
     * @return true if the service has been shut down.
     */
    val shutdown: Boolean  get() = status.shutDown

    /**
     * Initialize the service, do any required startup tasks, reserve resources, etc.
     * Must be called before the Service is used.
     */
    fun init() = init(null)

    /**
     * Initialize the service, do any required startup tasks, reserve resources, etc.
     * Must be called before the Service is used.
     * @param serviceProvider optional registry can be queried for other services.
     * Not all services have necessarily been initialized yet when this is called.
     */
    fun init(serviceProvider: ServiceProvider?)

    /**
     * Shuts down the service, frees any resources.
     * Must be called before the application closes.
     * After the Service has been shut down, it can not be initialized again.
     */
    fun shutdown()

}