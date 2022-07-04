package com.sksamuel.template.app

import mu.KotlinLogging
import org.flywaydb.core.Flyway
import javax.sql.DataSource

private val logger = KotlinLogging.logger { }

object FlywayDefaults {
   const val BaselineVersion = "0"
   const val BaselineOnMigrate = true
   const val ConnectRetries = 3
   const val Locations = "classpath:changesets"
}

fun flywayMigrate(ds: DataSource) {
   logger.info { "Initializing flyway migrations" }
   Flyway.configure()
      .dataSource(ds)
      .connectRetries(FlywayDefaults.ConnectRetries)
      .locations(FlywayDefaults.Locations)
      .baselineOnMigrate(FlywayDefaults.BaselineOnMigrate)
      .baselineVersion(FlywayDefaults.BaselineVersion)
      .load()
      .migrate()

}
