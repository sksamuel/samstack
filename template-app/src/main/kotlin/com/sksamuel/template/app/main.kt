package com.sksamuel.template.app

import com.sksamuel.cohort.HealthCheckRegistry
import com.sksamuel.cohort.ktor.Cohort
import com.sksamuel.cohort.ktor.EngineShutdownHook
import com.sksamuel.cohort.threads.ThreadDeadlockHealthCheck
import com.sksamuel.template.endpoints.module
import io.ktor.serialization.jackson.jackson
import io.ktor.server.application.install
import io.ktor.server.engine.embeddedServer
import io.ktor.server.metrics.micrometer.MicrometerMetrics
import io.ktor.server.netty.Netty
import io.ktor.server.plugins.compression.Compression
import io.ktor.server.plugins.contentnegotiation.ContentNegotiation
import io.ktor.server.routing.IgnoreTrailingSlash
import kotlinx.coroutines.Dispatchers
import mu.KotlinLogging
import kotlin.time.Duration.Companion.seconds

private val logger = KotlinLogging.logger { }

/**
 * In JVM apps, the main method is the entry point into the program.
 * Obviously printing out an ascii art banner is the most important part of any application.
 */
fun main() {
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

   val engineShutdownHook = EngineShutdownHook(prewait = 10.seconds, gracePeriod = 10.seconds, timeout = 30.seconds)
   val server = embeddedServer(Netty, port = App.config.port) {
      install(Compression)
      install(ContentNegotiation) { jackson() }
      install(IgnoreTrailingSlash)
      install(MicrometerMetrics) { this.registry = App.registry }
      install(Cohort) {
         this.gc = true
         this.jvmInfo = true
         this.sysprops = true
         this.threadDump = true
         this.heapDump = true
         onShutdown(engineShutdownHook)
         healthcheck("/health", HealthCheckRegistry(Dispatchers.Default) {
            register(ThreadDeadlockHealthCheck(2), 15.seconds)
         })
      }
      // create your http modules here, passing in dependencies from the context object.
      // each module can be a set of related endpoints and plugins that you can easily test.
      // you may only have a single module for your entire app.
      module(App.beerService)
   }
   engineShutdownHook.setEngine(server)
   server.start(true)
}
