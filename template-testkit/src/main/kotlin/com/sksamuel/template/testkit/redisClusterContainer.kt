package com.sksamuel.template.testkit

import io.kotest.extensions.testcontainers.SharedTestContainerExtension
import io.lettuce.core.ReadFrom
import io.lettuce.core.RedisURI
import io.lettuce.core.cluster.ClusterClientOptions
import io.lettuce.core.cluster.ClusterTopologyRefreshOptions
import io.lettuce.core.cluster.RedisClusterClient
import io.lettuce.core.cluster.SlotHash
import io.lettuce.core.cluster.api.StatefulRedisClusterConnection
import io.lettuce.core.resource.ClientResources
import io.lettuce.core.resource.SocketAddressResolver
import org.testcontainers.containers.GenericContainer
import java.net.InetSocketAddress
import java.net.SocketAddress
import kotlin.time.Duration.Companion.seconds
import kotlin.time.toJavaDuration

private const val DOCKER_IMAGE_NAME = "grokzen/redis-cluster:6.0.7"

private val redisContainer = GenericContainer(DOCKER_IMAGE_NAME)
   .withExposedPorts(7000, 7001, 7002, 7003, 7004, 7005)

val resolver = object : SocketAddressResolver() {
   override fun resolve(redisURI: RedisURI): SocketAddress {
      return if (redisURI.port > 7005) {
         InetSocketAddress.createUnresolved("localhost", redisURI.port)
      } else {
         redisURI.port = redisContainer.getMappedPort(redisURI.port)
         InetSocketAddress.createUnresolved("localhost", redisURI.port)
      }
   }
}

val redisCluster: SharedTestContainerExtension<GenericContainer<*>, StatefulRedisClusterConnection<String, String>> =
   SharedTestContainerExtension(redisContainer) {

      val uri = RedisURI.Builder.redis("localhost", 7000).build()

      val resources = ClientResources.builder()
         .socketAddressResolver(resolver)
         .build()

      val topologyRefreshOptions = ClusterTopologyRefreshOptions.builder()
         .enableAllAdaptiveRefreshTriggers()
         .adaptiveRefreshTriggersTimeout(1.seconds.toJavaDuration())
         .build()

      val clusterClient = RedisClusterClient.create(resources, uri).apply {
         setOptions(
            ClusterClientOptions
               .builder()
               .topologyRefreshOptions(topologyRefreshOptions)
               .build()
         )
      }

      fun openConnection(): StatefulRedisClusterConnection<String, String> {
         return try {
            val connection = clusterClient.connect().apply {
               this.readFrom = ReadFrom.ANY
            }
            val clusters = connection.sync().clusterNodes().trim().lines()
            require(clusters.size == 6) // 3 masters + 3 slaves
            val nodes = connection.sync().clusterSlots()
            val slots = nodes.sumOf {
               val bits = it as List<Any>
               bits[1].toString().toInt() - bits[0].toString().toInt() + 1
            }
            require(slots == SlotHash.SLOT_COUNT)
            // checks to ensure cluster has started, for each node
            connection.sync().get("a")
            connection.sync().get("b")
            connection.sync().get("c")
            connection
         } catch (e: Exception) {
            Thread.sleep(250)
            openConnection()
         }
      }

      openConnection()
   }
