package com.sksamuel.template.app

import com.sksamuel.cohort.ktor.Cohort
import com.sksamuel.cohort.ktor.EngineShutdownHook
import com.sksamuel.template.server.module
import io.ktor.server.application.install
import io.ktor.server.engine.embeddedServer
import io.ktor.server.metrics.micrometer.MicrometerMetrics
import io.ktor.server.netty.Netty
import io.ktor.server.netty.NettyApplicationEngine
import io.ktor.server.plugins.compression.Compression
import io.ktor.server.plugins.defaultheaders.DefaultHeaders
import io.ktor.server.plugins.hsts.HSTS
import io.ktor.server.routing.IgnoreTrailingSlash
import mu.KotlinLogging
import kotlin.time.Duration.Companion.hours

private val logger = KotlinLogging.logger { }

/**
 * Creates the ktor server instance for this application.
 * We use the [Netty] engine implementation.
 *
 * @return the engine instance ready to be started.
 */
fun createNettyServer(config: ServerConfig, app: App): NettyApplicationEngine {

   logger.info { "Creating Netty server @ https://localhost:${config.port}" }

   val engineShutdownHook = EngineShutdownHook(
      prewait = config.prewait,
      gracePeriod = config.grace,
      timeout = config.timeout,
   )

   val server = embeddedServer(Netty, port = config.port) {
      // adds server and date headers
      install(DefaultHeaders)

      // configures server side micrometer metrics
      install(MicrometerMetrics) { this.registry = app.registry }

      // allows foo/ and foo to be treated the same
      install(IgnoreTrailingSlash)

      // enables zip and deflate compression
      install(Compression)

      // enables strict security headers to force TLS
      install(HSTS) { maxAgeInSeconds = 1.hours.inWholeSeconds }

      // healthchecks and actuator endpoints
      install(Cohort) {
         this.gc = true
         this.jvmInfo = true
         this.sysprops = true
         this.threadDump = true
         this.heapDump = true
         onShutdown(engineShutdownHook)
         healthcheck("/startup", startupProbes(app.ds))
         healthcheck("/liveness", livenessProbes())
         healthcheck("/readiness", readinessProbes())
      }

      // create your http module here, passing in dependencies from the context object (or the deps object itself).
      // for a small enough microservice, you may want only a single module
      module(app.beerService)
   }
   engineShutdownHook.setEngine(server)
   return server
}
