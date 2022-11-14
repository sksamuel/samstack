package com.sksamuel.template.app

import com.sksamuel.cohort.ktor.Cohort
import com.sksamuel.template.server.beerEndpoints
import io.ktor.serialization.jackson.jackson
import io.ktor.server.application.install
import io.ktor.server.engine.addShutdownHook
import io.ktor.server.engine.embeddedServer
import io.ktor.server.metrics.micrometer.MicrometerMetrics
import io.ktor.server.netty.Netty
import io.ktor.server.netty.NettyApplicationEngine
import io.ktor.server.plugins.compression.Compression
import io.ktor.server.plugins.contentnegotiation.ContentNegotiation
import io.ktor.server.plugins.hsts.HSTS
import io.ktor.server.routing.IgnoreTrailingSlash
import io.ktor.server.routing.routing
import mu.KotlinLogging
import kotlin.time.Duration.Companion.hours

private val logger = KotlinLogging.logger { }

/**
 * Creates the ktor server instance for this application.
 * We use the [Netty] engine implementation.
 *
 * @return the engine instance ready to be started.
 */
fun createNettyServer(config: Config, dependencies: Dependencies): NettyApplicationEngine {

   logger.info { "Creating Netty server @ https://localhost:${config.port}" }

   val server = embeddedServer(Netty, port = config.port) {

      // configures server side micrometer metrics
      install(MicrometerMetrics) { this.registry = dependencies.registry }

      // allows foo/ and foo to be treated the same
      install(IgnoreTrailingSlash)

      // enables zip and deflate compression
      install(Compression)

      // setup json marshalling - provide your own jackson mapper if you have custom jackson modules
      install(ContentNegotiation) { jackson() }

      // enables strict security headers to force TLS
      install(HSTS) { maxAgeInSeconds = 1.hours.inWholeSeconds }

      // healthchecks and actuator endpoints
      install(Cohort) {
         this.gc = true
         this.jvmInfo = true
         this.sysprops = true
         this.threadDump = true
         this.heapDump = true
         healthcheck("/startup", startupProbes(dependencies.ds))
         healthcheck("/liveness", livenessProbes())
         healthcheck("/readiness", readinessProbes())
      }

      routing {
         beerEndpoints(dependencies.beerService)
      }
   }

   server.addShutdownHook {
      server.stop(config.quietPeriod.inWholeMilliseconds, config.shutdownTimeout.inWholeMilliseconds)
   }

   return server
}
