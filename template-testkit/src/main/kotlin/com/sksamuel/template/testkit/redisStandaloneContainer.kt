package com.sksamuel.template.testkit

import io.kotest.extensions.testcontainers.SharedTestContainerExtension
import io.lettuce.core.ClientOptions
import io.lettuce.core.RedisClient
import io.lettuce.core.RedisURI
import io.lettuce.core.SocketOptions
import io.lettuce.core.api.StatefulRedisConnection
import io.lettuce.core.resource.ClientResources
import org.testcontainers.containers.GenericContainer
import kotlin.time.Duration.Companion.seconds
import kotlin.time.toJavaDuration

private const val DOCKER_IMAGE_NAME = "redis:7.0.2"

private val redisContainer = GenericContainer(DOCKER_IMAGE_NAME)

val redisStandalone: SharedTestContainerExtension<GenericContainer<*>, StatefulRedisConnection<String, String>> =
   SharedTestContainerExtension(redisContainer) {

      val uri = RedisURI.Builder.redis(redisContainer.host, redisContainer.firstMappedPort).build()

      val resources = ClientResources.builder().build()

      val client = RedisClient.create(resources, uri).apply {
         options = ClientOptions.builder()
            .autoReconnect(true)
            .cancelCommandsOnReconnectFailure(true)
            .disconnectedBehavior(ClientOptions.DisconnectedBehavior.REJECT_COMMANDS)
            .socketOptions(
               SocketOptions.builder()
                  .connectTimeout(5.seconds.toJavaDuration())
                  .keepAlive(false)
                  .tcpNoDelay(true)
                  .build()
            ).build()
      }

      client.connect()
   }
