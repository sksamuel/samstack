package com.sksamuel.template.client

import io.ktor.client.HttpClient
import io.ktor.client.plugins.HttpClientPlugin
import io.ktor.client.request.HttpRequestPipeline
import io.ktor.http.encodedPath
import io.ktor.util.AttributeKey
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
                  "ktor.http.client.timer",
                  "url", context.url.encodedPath,
                  "method", context.method.value.lowercase(),
               ).record(time.toJavaDuration())
         }
      }
   }
}
