package com.sksamuel.template.app

import arrow.fx.coroutines.Resource
import arrow.fx.coroutines.continuations.resource
import arrow.fx.coroutines.fromCloseable
import arrow.fx.coroutines.release
import com.sksamuel.template.datastore.BeerDatastore
import com.sksamuel.template.datastore.createDataSource
import com.sksamuel.template.services.BeerService
import com.zaxxer.hikari.HikariDataSource
import io.micrometer.core.instrument.MeterRegistry

/**
 * Creates all the dependencies used by this application and returns in a container [Dependencies] 'god' object.
 *
 * Each managed resource is wrapped in a [Resource] object, and combined using the bind function,
 * so that they are all released gracefully together on shutdown.
 *
 * @param env the variable for the environment eg STAGING or PROD.
 * @param serviceName a unique name for this service used in logs and metrics
 * @param config the loaded configuration values.
 */
fun dependencies(env: String, serviceName: String, config: Config): Resource<Dependencies> = resource {

   // managed resources (ie, has state to shut down)
   val registry = resource { createMeterRegistry(config.datadog, env, serviceName) }.release { it.close() }.bind()
   val ds = Resource.fromCloseable { createDataSource(config.db, registry) }.bind()

   // unmanaged resources (ie no state requiring shut down)
   val beerDatastore = BeerDatastore(ds)
   val beerService = BeerService(beerDatastore)

   Dependencies(
      registry,
      ds,
      beerDatastore,
      beerService,
   )
}

data class Dependencies(
   val registry: MeterRegistry,
   val dataSource: HikariDataSource,
   val beerDatastore: BeerDatastore,
   val beerService: BeerService,
)
