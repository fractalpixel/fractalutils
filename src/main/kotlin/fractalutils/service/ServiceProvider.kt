package fractalutils.service

/**
 * Provides access to a set of services.
 */
interface ServiceProvider: Service {

    /**
     * @return the service of the specified type from this service provider or the parent provider, or null if not available.
     */
    fun <T: Service> getService(serviceType: Class<T>): T?

    /**
     * @return read only map from service types to available services.
     */
    val servicesMap: Map<Class<out Service>, Service>

    /**
     * @return read only collection with all services available.
     */
    val services: Collection<Service> get() = servicesMap.values

    /**
     * Adds a service to this ServiceProvider.
     * If there is already a service of the same type, an exception is thrown.
     * @param service service to add.  If this ServiceProvider has already been initialized, the newly added service will also be initialized.
     * @return the added service
     */
    fun <T: Service> addService(service: T): T

    /**
     * Removes the specified service type.
     * @return removed service, or null if none found
     */
    fun <T: Service> removeService(serviceType: Class<T>): T?

}