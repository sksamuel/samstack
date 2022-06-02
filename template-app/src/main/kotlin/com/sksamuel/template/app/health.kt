package com.sksamuel.template.app

import com.sksamuel.cohort.HealthCheckRegistry
import com.sksamuel.cohort.hikari.HikariConnectionsHealthCheck
import com.sksamuel.cohort.memory.FreememHealthCheck
import com.sksamuel.cohort.memory.GarbageCollectionTimeCheck
import com.sksamuel.cohort.threads.ThreadDeadlockHealthCheck
import com.sksamuel.cohort.threads.ThreadStateHealthCheck
import com.zaxxer.hikari.HikariDataSource
import kotlinx.coroutines.Dispatchers
import kotlin.time.Duration.Companion.seconds

// -- this file contains the various kubernetes health checks --
// -- startup is used while the app is initializing
// -- liveness is used to determine if or when the app has stalled and must be restarted
// -- readiness is used to temporarily halt new requests to a service if it is overloaded

fun startupProbes(ds: HikariDataSource) = HealthCheckRegistry(Dispatchers.Default) {
   register(ThreadDeadlockHealthCheck(2), 15.seconds)
   // we are not started until we have the min number of connections spun up
   register(HikariConnectionsHealthCheck(ds, ds.hikariConfigMXBean.minimumIdle), 15.seconds)
}

// this probe should be modified to check for scenarios that warrant a restart, such as low memory
val livenessProbes = HealthCheckRegistry(Dispatchers.Default) {
   register(ThreadDeadlockHealthCheck(2), 15.seconds)
   register(FreememHealthCheck(10_000_000), 15.seconds) // restart if less than 10mb available
   register(GarbageCollectionTimeCheck(50), 15.seconds) // restart if we are spending over 50% of time in gc
}

val readinessProbes = HealthCheckRegistry(Dispatchers.Default) {
   register(ThreadDeadlockHealthCheck(2), 15.seconds)
   register(ThreadStateHealthCheck(Thread.State.TIMED_WAITING, 50), 15.seconds)
   register(ThreadStateHealthCheck(Thread.State.WAITING, 50), 15.seconds)
}
