package com.sksamuel.template.app

import kotlinx.coroutines.DEBUG_PROPERTY_NAME
import kotlinx.coroutines.DEBUG_PROPERTY_VALUE_ON
import mu.KotlinLogging
import java.time.ZoneOffset
import java.util.TimeZone

private val logger = KotlinLogging.logger { }

suspend fun main() {

   // the APP_ENV env-var is used to determine which configuration files to load
   // we default to local so when running locally we don't need to specify the variable.
   val env = System.getenv("APP_ENV") ?: "local"
   logger.info("Environment=$env")

   // replace 'template-app' with the name of your app
   val serviceName = "template-app"
   logger.info("Service=$serviceName")

   // ensure we are always using UTC for times
   TimeZone.setDefault(TimeZone.getTimeZone(ZoneOffset.UTC))
   logger.info("Timezone=" + TimeZone.getDefault())

   // configure coroutines debug logging, useful for stack traces in coroutine land
   System.setProperty(DEBUG_PROPERTY_NAME, DEBUG_PROPERTY_VALUE_ON)
   logger.info("$DEBUG_PROPERTY_NAME=" + System.getProperty(DEBUG_PROPERTY_NAME))

   val config = config(env)
   dependencies(env, serviceName, config).use { deps ->
      val server = server(config, deps)
      server.start(wait = true)
   }
}
