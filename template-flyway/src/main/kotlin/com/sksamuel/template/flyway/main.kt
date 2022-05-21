package com.sksamuel.template.flyway

import com.sksamuel.hoplite.ConfigLoaderBuilder
import com.sksamuel.hoplite.PropertySource
import com.sksamuel.hoplite.aws.AwsSecretsManagerPreprocessor
import com.sksamuel.template.datastore.DatabaseConfig
import com.sksamuel.template.datastore.createDataSource
import mu.KotlinLogging
import org.flywaydb.core.Flyway

private val logger = KotlinLogging.logger { }

fun main() {

   // the ENV env-var is used to determine which configuration files to load
   // we default to local so when running locally we don't need to specify the variable.
   val env = System.getenv("ENV") ?: "local"
   val serviceName = "template-service"

   logger.info("Executing $serviceName database migrations in $env")

   // loads config from each config file in turn, cascading values from top to bottom.
   // that means that values in earlier files take precedence over values in later files.
   val config = ConfigLoaderBuilder.default()
      .addPropertySource(PropertySource.resource("/application-${env}.yml", true))
      .addPreprocessor(AwsSecretsManagerPreprocessor())
      .report() // shows config values at startup with strings obfuscated for security
      .build()
      .loadConfigOrThrow<Config>()

   // this is the generic java datasource object used for datastores
   // or remove completely if you are not using a database
   val ds = createDataSource(config.db, null)

   Flyway.configure()
      .dataSource(ds)
      .locations("classpath:changesets")
      .baselineOnMigrate(true)
      .baselineVersion("0")
      .load()
      .migrate()
}

data class Config(
   val db: DatabaseConfig,
)
