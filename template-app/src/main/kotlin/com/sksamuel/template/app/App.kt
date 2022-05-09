package com.sksamuel.template.app

import com.sksamuel.hoplite.ConfigLoaderBuilder
import com.sksamuel.hoplite.PropertySource
import com.sksamuel.hoplite.aws.AwsSecretsManagerPreprocessor
import com.sksamuel.template.myservice.datastore.BeerDatastore
import com.sksamuel.template.myservice.datastore.createDataSource
import com.sksamuel.template.services.BeerService
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
 * At startup, this object is initialized with your datasources, repositories,
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

   // replace 'template-app' with the name of your app
   private const val serviceName = "template-app"

   // the ENV env-var is used to determine which configuration files to load
   // we default to local so when running locally we don't need to specify the variable.
   private val env = System.getenv("ENV") ?: "local"

   // loads config from each config file in turn, cascading values from top to bottom.
   // that means that values in earlier files take precedence over values in later files.
   val config = ConfigLoaderBuilder.default()
      .addPropertySource(PropertySource.resource("/application-${env}.yml", true))
      .addPropertySource(PropertySource.resource("/reference.yml", true))
      .addPreprocessor(AwsSecretsManagerPreprocessor())
      .report() // shows config values at startup with strings obfuscated for security
      .build()
      .loadConfigOrThrow<Config>()

   // creates a datadog registry with tags set for env and service name
   val registry = createMeterRegistry(config.datadog, env, serviceName)

   // this is the generic java datasource object used for datastores
   // or remove completely if you are not using a database
   private val ds = createDataSource(config.db, registry)

   val beerService: BeerService = BeerService(BeerDatastore(ds))
}
