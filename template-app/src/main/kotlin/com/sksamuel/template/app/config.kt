package com.sksamuel.template.app

import com.sksamuel.hoplite.ConfigLoaderBuilder
import com.sksamuel.hoplite.PropertySource
import com.sksamuel.hoplite.aws.AwsSecretsManagerPreprocessor
import com.sksamuel.template.datastore.DatabaseConfig
import kotlin.time.Duration

/**
 * Loads config from each config file in turn, cascading values from top to bottom.
 * This means that values in earlier files take precedence over values in later files.
 *
 * @param env value used to specify which environment file(s) to load, eg PROD or STAGING.
 * @return the constructed [Config] object.
 */
fun config(env: String) = ConfigLoaderBuilder.default()
   .addPreprocessor(AwsSecretsManagerPreprocessor())
   .addPropertySource(PropertySource.resource("/application-${env}.yml", true))
   .addPropertySource(PropertySource.resource("/shared.yml", true)) // shared config goes in shared.yml
   .report() // shows config values at startup with strings obfuscated for security
   .build()
   .loadConfigOrThrow<Config>()

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
   val shutdown: ShutdownDurations,
   val datadog: DatadogHttpConfig,
   val db: DatabaseConfig,
)

data class ShutdownDurations(
   val prewait: Duration,
   val grace: Duration,
   val timeout: Duration,
)
