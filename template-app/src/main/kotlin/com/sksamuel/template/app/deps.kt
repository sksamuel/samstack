package com.sksamuel.template.app

import arrow.fx.coroutines.Resource
import arrow.fx.coroutines.continuations.resource
import arrow.fx.coroutines.release
import com.sksamuel.hoplite.env.Environment
import com.sksamuel.template.datastore.BeerDatastore
import com.sksamuel.template.datastore.createDataSource
import com.sksamuel.template.server.BeerService
import com.zaxxer.hikari.HikariDataSource
import io.micrometer.core.instrument.MeterRegistry
import mu.KotlinLogging

private val logger = KotlinLogging.logger { }

/**
 * Creates all the dependencies used by this service wrapped in a [App] object.
 *
 * Each managed resource is wrapped in a [Resource] object, which are combined using the bind function,
 * so that they are all released gracefully together on shutdown.
 *
 * If any of the resources cannot be acquired, then the returned [Resource] will fail.
 *
 * @param env the variable for the environment eg STAGING or PROD.
 * @param serviceName a unique name for this service used in logs and metrics
 * @param config the loaded configuration values.
 */
fun createDependencies(env: Environment, serviceName: String, config: Config): Resource<App> = resource {

   // -- managed resources (these have state to shut down) --

   val registry = resource {
      createDatadogMeterRegistry(config.datadog, env, serviceName)
   }.release {
      logger.info { "Closing meter registry" }
      it.close()
   }.bind()

   val ds = resource {
      createDataSource(config.db, registry)
   }.release {
      logger.info { "Closing datasource" }
      it.close()
   }.bind()

   // -- unmanaged resources (these have no state to shut down so do not need to be wrapped in resource blocks) --

   val beerDatastore = BeerDatastore(ds)
   val beerService = BeerService(beerDatastore)

   App(
      registry,
      ds,
      beerDatastore,
      beerService,
   )
}

/**
 * The [App] object is a god object that contains all the dependencies of the project.
 *
 * In an dependency injection framework like Spring, this is created automagically for you and is
 * called ApplicationContext.
 */
data class App(
   val registry: MeterRegistry,
   val ds: HikariDataSource,
   val beerDatastore: BeerDatastore,
   val beerService: BeerService,
)
