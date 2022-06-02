package com.sksamuel.template.app

import com.sksamuel.cohort.ktor.Cohort
import com.sksamuel.cohort.ktor.EngineShutdownHook
import com.sksamuel.template.endpoints.module
import io.ktor.serialization.jackson.jackson
import io.ktor.server.application.install
import io.ktor.server.engine.embeddedServer
import io.ktor.server.metrics.micrometer.MicrometerMetrics
import io.ktor.server.netty.Netty
import io.ktor.server.netty.NettyApplicationEngine
import io.ktor.server.plugins.compression.Compression
import io.ktor.server.plugins.contentnegotiation.ContentNegotiation
import io.ktor.server.routing.IgnoreTrailingSlash

/**
 * Creates the ktor server instance for this application.
 * We use the [NettyApplicationEngine] engine implementation.
 */
fun server(config: Config, deps: Dependencies): NettyApplicationEngine {

   val engineShutdownHook = EngineShutdownHook(
      prewait = config.shutdown.prewait,
      gracePeriod = config.shutdown.grace,
      timeout = config.shutdown.timeout
   )

   val server = embeddedServer(Netty, port = config.port) {
      install(Compression)
      install(ContentNegotiation) { jackson() }
      install(IgnoreTrailingSlash) // allows foo/ and foo to be treated the same
      install(MicrometerMetrics) { this.registry = deps.registry }
      install(Cohort) {
         this.gc = true
         this.jvmInfo = true
         this.sysprops = true
         this.threadDump = true
         this.heapDump = true
         onShutdown(engineShutdownHook)
         healthcheck("/startup", startupProbes(deps.dataSource))
         healthcheck("/liveness", livenessProbes)
         healthcheck("/readiness", readinessProbes)
      }
      // create your http modules here, passing in dependencies from the context object.
      // each module can be a set of related endpoints and plugins that you can easily test.
      // you may only have a single module for your entire app.
      module(deps.beerService)
   }
   engineShutdownHook.setEngine(server)
   return server
}
