package com.sksamuel.template.app

import com.sksamuel.hoplite.env.Environment
import com.sksamuel.template.datastore.flywayMigrate
import kotlinx.coroutines.DEBUG_PROPERTY_NAME
import kotlinx.coroutines.DEBUG_PROPERTY_VALUE_ON
import mu.KotlinLogging
import java.time.ZoneOffset
import java.util.TimeZone

private val logger = KotlinLogging.logger { }

fun main() {

   logger.info {
      """
████████╗███████╗███╗   ███╗██████╗ ██╗      █████╗ ████████╗███████╗    ███████╗███████╗██████╗ ██╗   ██╗██╗ ██████╗███████╗
╚══██╔══╝██╔════╝████╗ ████║██╔══██╗██║     ██╔══██╗╚══██╔══╝██╔════╝    ██╔════╝██╔════╝██╔══██╗██║   ██║██║██╔════╝██╔════╝
   ██║   █████╗  ██╔████╔██║██████╔╝██║     ███████║   ██║   █████╗█████╗███████╗█████╗  ██████╔╝██║   ██║██║██║     █████╗
   ██║   ██╔══╝  ██║╚██╔╝██║██╔═══╝ ██║     ██╔══██║   ██║   ██╔══╝╚════╝╚════██║██╔══╝  ██╔══██╗╚██╗ ██╔╝██║██║     ██╔══╝
   ██║   ███████╗██║ ╚═╝ ██║██║     ███████╗██║  ██║   ██║   ███████╗    ███████║███████╗██║  ██║ ╚████╔╝ ██║╚██████╗███████╗
   ╚═╝   ╚══════╝╚═╝     ╚═╝╚═╝     ╚══════╝╚═╝  ╚═╝   ╚═╝   ╚══════╝    ╚══════╝╚══════╝╚═╝  ╚═╝  ╚═══╝  ╚═╝ ╚═════╝╚══════╝
"""
   }

   // the ENV_NAME environment variable is used to determine which configuration files to load
   // this must be set in your helm charts when deploying to a real environment.
   // we default to local so when running locally we don't need to specify the variable.
   val env = Environment.fromEnvVar("ENV_NAME", fallback = Environment.local)

   // replace 'template-app' with the name of your app, eg "registration-service"
   // this name is used for logging and metrics
   val serviceName = "template-app"

   // ensure we are always using UTC for times
   TimeZone.setDefault(TimeZone.getTimeZone(ZoneOffset.UTC))

   // configure coroutines debug logging, useful for stack traces thrown inside coroutines
   System.setProperty(DEBUG_PROPERTY_NAME, DEBUG_PROPERTY_VALUE_ON)

   val config = createConfig(env)
   val deps = createDependencies(env, serviceName, config)

   // we use the APP_TYPE environment variable to determine app type
   // if not specified then we start the http server
   when (System.getenv("APP_TYPE")) {
      "flyway" -> flywayMigrate(deps.ds)
      else -> {
         val server = createNettyServer(config, deps)
         server.start(wait = true)
      }
   }
}
