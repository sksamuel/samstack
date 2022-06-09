package com.sksamuel.template.datastore

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import io.micrometer.core.instrument.MeterRegistry

data class DatabaseConfig(
   val url: String,
   val username: String,
   val password: String,
   val maximumPoolSize: Int = 8,
   val minimumIdle: Int = 2,
   val idleTimeout: Long? = null,
   val maxLifetime: Long? = null,
   val poolName: String? = null,
)

/**
 * Creates the [HikariDataSource] connection pool.
 * Can be removed if you are not using a database in this application.
 */
fun createDataSource(config: DatabaseConfig, registry: MeterRegistry?): HikariDataSource {
   val hikariConfig = HikariConfig()

   hikariConfig.jdbcUrl = config.url
   hikariConfig.username = config.username
   hikariConfig.password = config.password

   // maximum number of connections
   hikariConfig.maximumPoolSize = config.maximumPoolSize

   // keep at least this number of connections open
   hikariConfig.minimumIdle = config.minimumIdle

   // default is 30 minutes
   config.maxLifetime?.let { hikariConfig.maxLifetime = it }

   // how long a connection is unused before being shut down
   config.idleTimeout?.let { hikariConfig.idleTimeout = it }

   // in systems with multiple datasources, like readers and writers, can be useful to give
   // them a name for metrics purposes
   if (config.poolName != null) hikariConfig.poolName = config.poolName

   // this wires in micrometer metrics
   hikariConfig.metricRegistry = registry

   return HikariDataSource(hikariConfig)
}
