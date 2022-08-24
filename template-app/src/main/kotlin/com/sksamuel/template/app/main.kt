package com.sksamuel.template.app

import com.sksamuel.hoplite.env.Environment
import com.sksamuel.template.datastore.flywayMigrate
import io.ktor.server.engine.addShutdownHook
import io.ktor.server.engine.stop
import kotlinx.coroutines.DEBUG_PROPERTY_NAME
import kotlinx.coroutines.DEBUG_PROPERTY_VALUE_ON
import mu.KotlinLogging
import java.time.ZoneOffset
import java.util.TimeZone
import java.util.concurrent.TimeUnit
import kotlin.time.Duration.Companion.seconds

private val logger = KotlinLogging.logger { }

suspend fun main() {

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
   logger.info("Environment=$env")

   // replace 'template-app' with the name of your app, eg "registration-service"
   // this name is used for logging and metrics
   val serviceName = "template-app"
   logger.info("Service=$serviceName")

   // ensure we are always using UTC for times
   TimeZone.setDefault(TimeZone.getTimeZone(ZoneOffset.UTC))
   logger.info("Timezone=" + TimeZone.getDefault())

   // configure coroutines debug logging, useful for stack traces thrown inside coroutines
   System.setProperty(DEBUG_PROPERTY_NAME, DEBUG_PROPERTY_VALUE_ON)
   logger.info("$DEBUG_PROPERTY_NAME=" + System.getProperty(DEBUG_PROPERTY_NAME))

   val config = createConfig(env)
   createDependencies(env, serviceName, config).use { app ->

      // we use the APP_TYPE environment variable to determine app type
      // if not specified then we start the http server
      when (System.getenv("APP_TYPE")) {
         "flyway" -> flywayMigrate(app.ds)
         else -> {
            val server = createNettyServer(config, app)
            server.addShutdownHook {
               println("Shutting down server")
               server.stop(5.seconds.inWholeSeconds, 20.seconds.inWholeSeconds, TimeUnit.SECONDS)
            }
            server.start(wait = true)
            println("Server stopped")
         }
      }
   }
}
