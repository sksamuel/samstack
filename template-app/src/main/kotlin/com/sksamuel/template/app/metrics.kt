package com.sksamuel.template.app

import com.sksamuel.hoplite.env.Environment
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

private val logger = KotlinLogging.logger { }

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

   // the hostname tag is a special tag that dd uses to distinguish pods
   const val HostnameTag = "hostname"
   const val HostnameEnvVar = "HOSTNAME"
}

/**
 * Creates a Micrometer [MeterRegistry] backed by a datadog collector.
 *
 * @param config datadog config loaded from Hoplite
 * @param env sets the environment name, eg STAGING or PROD.
 * @param serviceName a unique name for this microservice, eg "foo-service"
 */
fun createDatadogMeterRegistry(config: DatadogConfig, env: Environment, serviceName: String): DatadogMeterRegistry {

   // creates a datadog http based registry
   val registry = DatadogMeterRegistry(config, Clock.SYSTEM)

   // HostnameEnvVar should be set in the helm chart, otherwise we try to look it up
   val hostname = System.getenv(DatadogDefaults.HostnameEnvVar) ?: InetAddress.getLocalHost().hostName

   // tags we attach to all metrics
   // can increase the cost of datadog by increasing the total number of custom metrics
   mapOf(
      DatadogDefaults.AppNameTag to serviceName,
      DatadogDefaults.EnvTag to env.name,
      DatadogDefaults.HostnameTag to hostname,
   ).forEach { (key, value) ->
      if (value != null) {
         logger.info { "Datadog common tag $key=$value" }
         registry.config().commonTags(listOf(ImmutableTag(key, value)))
      }
   }

   metrics.forEach { it.bindTo(registry) }
   return registry
}

