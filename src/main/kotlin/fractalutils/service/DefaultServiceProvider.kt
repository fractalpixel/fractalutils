package fractalutils.service

import java.util.*

/**
 * Typically used as a base class for the central controller of an application.
 * Add services using [addService], and start all services by calling [init].
 * Shutdown everything by calling [shutdown].
 *
 * Service providers can be added as services to others, in which case they form a
 * hierarchical tree (getting a service will look for it in the parent provider if
 * it is not found in this provider).
 * Usually a tree of service providers is not necessary, however.
 */
open class DefaultServiceProvider(): ServiceBase(), ServiceProvider {

    private var parentServiceProvider: ServiceProvider? = null
    override val servicesMap = LinkedHashMap<Class<out Service>, Service>()

    /**
     * If overridden, remember to call the parent, as it initializes all added services.
     */
    override fun doInit(serviceProvider: ServiceProvider?) {
        parentServiceProvider = serviceProvider

        for (service in services) {
            service.init(this)
        }
    }

    /**
     * If overridden, remember to call the parent, as it shutdowns all added services.
     */
    override fun doShutdown() {
        for (service in services.reversed()) {
            service.shutdown()
        }
    }

    override fun <T: Service> getService(serviceType: Class<T>): T? {
        val service = servicesMap[serviceType]
        @Suppress("UNCHECKED_CAST")
        return if (service != null) service as T
               else parentServiceProvider?.getService(serviceType)
    }

    override fun <T: Service> addService(service: T): T {
        val key = service.javaClass
        val existingService = getService(key)
        if (existingService != null) throw IllegalArgumentException("A service of type $key is already registered with this service provider (or its parent)")

        servicesMap.put(key, service)
        if (active) service.init(this)
        return service
    }

    override fun <T: Service> removeService(serviceType: Class<T>): T? {
        val existingService = servicesMap.remove(serviceType)

        if (existingService != null) {
            if (existingService.active) existingService.shutdown()
        }

        @Suppress("UNCHECKED_CAST")
        return existingService as T?
    }
}