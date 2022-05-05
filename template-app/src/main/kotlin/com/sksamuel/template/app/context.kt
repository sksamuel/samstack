package com.sksamuel.template.app

import com.sksamuel.template.myservice.datastore.createDataSource
import kotlinx.coroutines.DEBUG_PROPERTY_NAME
import kotlinx.coroutines.DEBUG_PROPERTY_VALUE_ON
import mu.KotlinLogging
import java.time.ZoneOffset
import java.util.*

private val logger = KotlinLogging.logger { }

/**
 * [App] contains all your top level dependencies.
 * You think of this as your god object.
 *
 * At startup, this object is initialized with your datasource, repositories,
 * services, registry, etc.
 */
object App {

   init {
      // configure coroutines debug logging, useful for stack traces in coroutine land
      System.setProperty(DEBUG_PROPERTY_NAME, DEBUG_PROPERTY_VALUE_ON)
      logger.info("$DEBUG_PROPERTY_NAME=" + System.getProperty(DEBUG_PROPERTY_NAME))

      // ensure we are always using UTC for times
      TimeZone.setDefault(TimeZone.getTimeZone(ZoneOffset.UTC))
      logger.info("Timezone=" + TimeZone.getDefault())
   }

   // creates a datadog registry with tags set for env and service name
   val registry = createMeterRegistry(Environment.config.datadog, Environment.env, Environment.serviceName)

   // this is the generic java datasource object used for datastores
   // or remove completely if you are not using a database
   private val ds = createDataSource(Environment.config.db, registry)
}
