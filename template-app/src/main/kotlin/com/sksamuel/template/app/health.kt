package com.sksamuel.template.app

import com.sksamuel.cohort.HealthCheckRegistry
import com.sksamuel.cohort.cpu.ProcessCpuHealthCheck
import com.sksamuel.cohort.hikari.HikariConnectionsHealthCheck
import com.sksamuel.cohort.memory.FreememHealthCheck
import com.sksamuel.cohort.memory.GarbageCollectionTimeCheck
import com.sksamuel.cohort.system.OpenFileDescriptorsHealthCheck
import com.sksamuel.cohort.threads.ThreadDeadlockHealthCheck
import com.sksamuel.cohort.threads.ThreadStateHealthCheck
import com.zaxxer.hikari.HikariDataSource
import kotlinx.coroutines.Dispatchers
import kotlin.time.Duration.Companion.seconds

// -- this file contains the various kubernetes health checks --

/**
 * These probes determine when the app has finished initializing. For example, we may want to wait
 * until we have connected to a database, or an elasticsearch instance, or performed some other
 * expensive startup cost.
 */
fun startupProbes(ds: HikariDataSource) = HealthCheckRegistry(Dispatchers.Default) {
   register(ThreadDeadlockHealthCheck(2), 15.seconds)
   // we are not started until we have the min number of connections spun up
   register(HikariConnectionsHealthCheck(ds, ds.hikariConfigMXBean.minimumIdle), 15.seconds)
   // we don't consider ourselves started until the CPU has settled down
   register(ProcessCpuHealthCheck(0.5), 15.seconds)
}

/**
 * These probes should be used to check for scenarios that warrant a restart, such as out of memory,
 * thread deadlocks, or too many open file descriptors.
 */
fun livenessProbes() = HealthCheckRegistry(Dispatchers.Default) {
   register(ThreadDeadlockHealthCheck(2), 15.seconds)
   register(OpenFileDescriptorsHealthCheck(32_000), 15.seconds)
   register(FreememHealthCheck(10_000_000), 15.seconds) // restart if less than 10mb available
   register(GarbageCollectionTimeCheck(50), 15.seconds) // restart if we are spending over 50% of time in gc
}

/**
 * Add probes here that should determine when to temporarily halt new requests to a service.
 * This is used when a service is overloaded but does not require a restart.
 */
fun readinessProbes() = HealthCheckRegistry(Dispatchers.Default) {
   register(ThreadDeadlockHealthCheck(2), 15.seconds)
   register(ThreadStateHealthCheck(Thread.State.TIMED_WAITING, 50), 15.seconds)
   register(ThreadStateHealthCheck(Thread.State.WAITING, 50), 15.seconds)
}
