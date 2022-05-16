package com.sksamuel.template.app

import com.sksamuel.template.datastore.DatabaseConfig

/**
 * [Config] models the values from the config files.
 *
 * Add stand-alone values or further nested config classes here.
 *
 * For example, you could create a RedisConfig class:
 * `data class RedisConfig(val host: String, val port: Int)`
 *
 * Then you could add that to this config object:
 * `val redis: RedisConfig`
 *
 * Then in the config files you would need to define the redis section like this (if using yaml):
 *
 * redis:
 *   host: localhost
 *   port: 12345
 *
 */
data class Config(
   val port: Int,
   val datadog: DatadogHttpConfig,
   val db: DatabaseConfig,
)
