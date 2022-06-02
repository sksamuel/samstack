package com.sksamuel.template.datastore

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import io.micrometer.core.instrument.MeterRegistry
import kotlin.time.Duration
import kotlin.time.Duration.Companion.hours

data class DatabaseConfig(
   val url: String,
   val username: String,
   val password: String,
   val maximumPoolSize: Int = 8,
   val minimumIdle: Int = 2,
   val idleTimeout: Duration = 1.hours,
   val maxLifetime: Duration = 1.hours,
   val cachePrepStmts: Boolean = true,
   val prepStmtCacheSize: Int = 1,
   val prepStmtCacheSqlLimit: Int = 1,
   val poolName: String?,
)

/**
 * Creates the [HikariDataSource] connectoion pool.
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
   hikariConfig.maxLifetime = config.maxLifetime.inWholeMilliseconds

   // how long a connection is unused before being shut down
   hikariConfig.idleTimeout = config.idleTimeout.inWholeMilliseconds

   // these are properties on the underlying JDBC connection
   hikariConfig.addDataSourceProperty("cachePrepStmts", config.cachePrepStmts)
   hikariConfig.addDataSourceProperty("prepStmtCacheSize", config.prepStmtCacheSize)
   hikariConfig.addDataSourceProperty("prepStmtCacheSqlLimit", config.prepStmtCacheSqlLimit)

   // in systems with multiple datasources, like readers and writers, can be useful to give
   // them a name for metrics purposes
   if (config.poolName != null) hikariConfig.poolName = config.poolName

   // this wires in micrometer metrics
   hikariConfig.metricRegistry = registry

   return HikariDataSource(hikariConfig)
}
