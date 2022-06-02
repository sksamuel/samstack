package com.sksamuel.template.app

import arrow.fx.coroutines.Resource
import arrow.fx.coroutines.continuations.resource
import arrow.fx.coroutines.fromCloseable
import com.sksamuel.template.datastore.BeerDatastore
import com.sksamuel.template.datastore.DatabaseConfig
import com.sksamuel.template.datastore.createDataSource
import com.sksamuel.template.services.BeerService
import com.zaxxer.hikari.HikariDataSource
import io.micrometer.core.instrument.MeterRegistry

fun dependencies(registry: MeterRegistry, config: Config) = resource {
   val ds = datasource(config.db, registry).bind()
   val beerDatastore = BeerDatastore(ds)
   val beerService = BeerService(beerDatastore)
   Dependencies(
      registry,
      ds,
      beerDatastore,
      beerService,
   )
}

// this is the generic java datasource object used for datastores
// or remove completely if you are not using a database
fun datasource(databaseConfig: DatabaseConfig, registry: MeterRegistry) =
   Resource.fromCloseable { createDataSource(databaseConfig, registry) }

data class Dependencies(
   val registry: MeterRegistry,
   val dataSource: HikariDataSource,
   val beerDatastore: BeerDatastore,
   val beerService: BeerService,
)
