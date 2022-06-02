package com.sksamuel.template.app

import io.micrometer.core.instrument.Clock
import io.micrometer.core.instrument.ImmutableTag
import io.micrometer.core.instrument.MeterRegistry
import io.micrometer.core.instrument.binder.jvm.ClassLoaderMetrics
import io.micrometer.core.instrument.binder.jvm.JvmCompilationMetrics
import io.micrometer.core.instrument.binder.jvm.JvmGcMetrics
import io.micrometer.core.instrument.binder.jvm.JvmHeapPressureMetrics
import io.micrometer.core.instrument.binder.jvm.JvmInfoMetrics
import io.micrometer.core.instrument.binder.jvm.JvmMemoryMetrics
import io.micrometer.core.instrument.binder.jvm.JvmThreadMetrics
import io.micrometer.core.instrument.binder.system.DiskSpaceMetrics
import io.micrometer.core.instrument.binder.system.FileDescriptorMetrics
import io.micrometer.core.instrument.binder.system.ProcessorMetrics
import io.micrometer.core.instrument.binder.system.UptimeMetrics
import io.micrometer.datadog.DatadogConfig
import io.micrometer.datadog.DatadogMeterRegistry
import mu.KotlinLogging
import java.io.File
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
   ClassLoaderMetrics(),
   DiskSpaceMetrics(File("/")),
   FileDescriptorMetrics(),
   JvmCompilationMetrics(),
   JvmGcMetrics(),
   JvmInfoMetrics(),
   JvmHeapPressureMetrics(),
   JvmMemoryMetrics(),
   JvmThreadMetrics(),
   ProcessorMetrics(),
   UptimeMetrics(),
)

fun createMeterRegistry(config: DatadogHttpConfig, env: String, service: String): MeterRegistry {

   // creates a datadog http based registry
   val registry = DatadogMeterRegistry(object : DatadogConfig {
      override fun apiKey(): String = config.apiKey
      override fun applicationKey(): String? = config.applicationKey
      override fun batchSize(): Int = 1000
      override fun enabled(): Boolean = config.enabled
      override fun hostTag(): String = "hostname"
      override fun get(key: String): String? = null
      override fun step(): Duration = Duration.ofSeconds(30)
   }, Clock.SYSTEM)

   // HOSTNAME should be set in the helm chart, otherwise it will try to look it up
   val hostname = System.getenv("HOSTNAME") ?: InetAddress.getLocalHost().hostName
   logger.info("Hostname=$hostname")

   // PODNAME is optional and if required, must be set by your helm chart
   val podname = System.getenv("PODNAME")
   logger.info("Podname=$podname")

   // tags we attach to all metrics
   // can add to expense by expanding out the total number of tags
   mapOf(
      "service" to service,
      "env" to env,
      "hostname" to hostname,
      "podname" to podname,
   ).forEach { (key, value) ->
      if (value != null) registry.config().commonTags(listOf(ImmutableTag(key, value)))
   }

   metrics.forEach { it.bindTo(registry) }
   return registry
}

