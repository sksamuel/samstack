package com.sksamuel.template.app

import kotlinx.coroutines.DEBUG_PROPERTY_NAME
import kotlinx.coroutines.DEBUG_PROPERTY_VALUE_ON
import mu.KotlinLogging
import java.time.ZoneOffset
import java.util.*

private val logger = KotlinLogging.logger { }

// the ENV env-var is used to determine which configuration files to load
// we default to local so when running locally we don't need to specify the variable.
val env = System.getenv("ENV") ?: "local"

// replace 'template-app' with the name of your app
const val serviceName = "template-app"

/**
 * In JVM apps, the main method is the entry point into the program.
 * Obviously, printing out an ascii banner is the most important part of any application.
 */
suspend fun main() {

   // configure coroutines debug logging, useful for stack traces in coroutine land
   System.setProperty(DEBUG_PROPERTY_NAME, DEBUG_PROPERTY_VALUE_ON)
   logger.info("$DEBUG_PROPERTY_NAME=" + System.getProperty(DEBUG_PROPERTY_NAME))

   // ensure we are always using UTC for times
   TimeZone.setDefault(TimeZone.getTimeZone(ZoneOffset.UTC))
   logger.info("Timezone=" + TimeZone.getDefault())

   logger.info(
      """
███╗   ███╗██╗   ██╗     ███████╗███████╗██████╗ ██╗   ██╗██╗ ██████╗███████╗
████╗ ████║╚██╗ ██╔╝     ██╔════╝██╔════╝██╔══██╗██║   ██║██║██╔════╝██╔════╝
██╔████╔██║ ╚████╔╝█████╗███████╗█████╗  ██████╔╝██║   ██║██║██║     █████╗
██║╚██╔╝██║  ╚██╔╝ ╚════╝╚════██║██╔══╝  ██╔══██╗╚██╗ ██╔╝██║██║     ██╔══╝
██║ ╚═╝ ██║   ██║        ███████║███████╗██║  ██║ ╚████╔╝ ██║╚██████╗███████╗
╚═╝     ╚═╝   ╚═╝        ╚══════╝╚══════╝╚═╝  ╚═╝  ╚═══╝  ╚═╝ ╚═════╝╚══════╝
"""
   )

   val config = config(env)
   dependencies(env, serviceName, config).use { deps ->
      val server = server(config, deps)
      server.start(wait = true)
   }
}
