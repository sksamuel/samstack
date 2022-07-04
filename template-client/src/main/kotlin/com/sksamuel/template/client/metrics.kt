package com.sksamuel.template.client

import io.ktor.client.HttpClient
import io.ktor.client.plugins.HttpClientPlugin
import io.ktor.client.request.HttpRequestPipeline
import io.ktor.client.statement.HttpReceivePipeline
import io.ktor.client.statement.request
import io.ktor.http.encodedPath
import io.ktor.util.AttributeKey
import io.ktor.util.KtorDsl
import io.micrometer.core.instrument.MeterRegistry
import io.micrometer.core.instrument.simple.SimpleMeterRegistry
import kotlin.time.measureTime
import kotlin.time.toJavaDuration

/**
 * Micrometer plugin for [HttpClient].
 *
 * NOTE: Do not rename the metric keys, otherwise they will not be uniform across all our services.
 *
 * @param registry the register to send metrics to
 */
class Micrometer internal constructor(
   private val registry: MeterRegistry,
) {

   class Config(var registry: MeterRegistry = SimpleMeterRegistry())

   /**
    * A companion object used to install a plugin.
    */
   @KtorDsl
   companion object Plugin : HttpClientPlugin<Config, Micrometer> {

      override val key: AttributeKey<Micrometer> = AttributeKey("Micrometer")

      override fun prepare(block: Config.() -> Unit): Micrometer {
         val config = Config().apply(block)
         return Micrometer(config.registry)
      }

      override fun install(plugin: Micrometer, scope: HttpClient) {

         scope.requestPipeline.intercept(HttpRequestPipeline.Before) {
            val time = measureTime { proceed() }
            plugin.registry
               .timer(
                  "ktor.client.timer",
                  "url", context.url.encodedPath,
                  "method", context.method.value.lowercase()
               ).record(time.toJavaDuration())
         }

         scope.receivePipeline.intercept(HttpReceivePipeline.State) {
            plugin.registry.counter(
               "ktor.client.status",
               "url", it.request.url.encodedPath,
               "method", it.request.method.value.lowercase(),
               "status", it.status.value.toString()
            ).increment()
            proceed()
         }
      }
   }
}
