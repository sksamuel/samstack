package com.sksamuel.template.app

import io.micrometer.core.instrument.Clock
import io.micrometer.core.instrument.ImmutableTag
import io.micrometer.core.instrument.MeterRegistry
import io.micrometer.core.instrument.binder.jvm.JvmMemoryMetrics
import io.micrometer.core.instrument.binder.jvm.JvmThreadMetrics
import io.micrometer.core.instrument.binder.system.FileDescriptorMetrics
import io.micrometer.core.instrument.binder.system.UptimeMetrics
import io.micrometer.datadog.DatadogConfig
import io.micrometer.datadog.DatadogMeterRegistry
import mu.KotlinLogging
import java.net.InetAddress
import java.time.Duration

private val logger = KotlinLogging.logger { }

data class DatadogHttpConfig(
   val enabled: Boolean,
   val apiKey: String,
   // The Datadog application key. This is only required if you care for metadata like base units, description,
   // and meter type to be published to Datadog.
   val applicationKey: String?,
)

// these can be removed if you are using APMs from a JVM agent
private val metrics = listOf(
   FileDescriptorMetrics(),
   JvmMemoryMetrics(),
   JvmThreadMetrics(),
   UptimeMetrics(),
)

object DatadogDefaults {

   const val AppNameTag = "service"
   const val EnvTag = "env"
   const val PodnameTag = "podname"

   // the hostname tag is a special tag that dd uses to distinguish pods
   const val HostnameTag = "hostname"

   // default is 10,000 smaller size means more requests but less chance of a timeout
   const val BatchSize = 1000

   const val HostnameEnvVar = "HOSTNAME"
   const val PodnameEnvVar = "PODNAME"
}

/**
 * Creates the Micrometer [MeterRegistry] backed by a datadog collector.
 *
 * @param config datadog API keys
 * @param env sets the environment name, eg STAGING or PROD.
 * @param serviceName a unique name for this microservice, eg "registration-service"
 */
fun createMeterRegistry(config: DatadogHttpConfig, env: String, serviceName: String): MeterRegistry {

   // creates a datadog http based registry
   val registry = DatadogMeterRegistry(object : DatadogConfig {
      override fun apiKey(): String = config.apiKey

      // required to pass units, not usually required
      override fun applicationKey(): String? = config.applicationKey
      override fun batchSize(): Int = DatadogDefaults.BatchSize
      override fun enabled(): Boolean = config.enabled
      override fun hostTag(): String = DatadogDefaults.HostnameTag
      override fun get(key: String): String? = null

      // how often to publish to datadog
      override fun step(): Duration = Duration.ofSeconds(30)
   }, Clock.SYSTEM)

   // HostnameEnvVar should be set in the helm chart, otherwise we try to look it up
   val hostname = System.getenv(DatadogDefaults.HostnameEnvVar) ?: InetAddress.getLocalHost().hostName

   // PodnameEnvVar is optional and if required, must be set by your helm chart
   val podname = System.getenv(DatadogDefaults.PodnameEnvVar)

   // tags we attach to all metrics
   // can increase the cost of datadog by increasing the total number of custom metrics
   mapOf(
      DatadogDefaults.AppNameTag to serviceName,
      DatadogDefaults.EnvTag to env,
      DatadogDefaults.HostnameTag to hostname,
      DatadogDefaults.PodnameTag to podname,
   ).forEach { (key, value) ->
      if (value != null) {
         logger.info { "Datadog common tag $key=$value" }
         registry.config().commonTags(listOf(ImmutableTag(key, value)))
      }
   }

   metrics.forEach { it.bindTo(registry) }
   return registry
}

