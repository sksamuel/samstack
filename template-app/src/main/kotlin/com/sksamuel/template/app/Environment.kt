package com.sksamuel.template.app

import com.sksamuel.hoplite.ConfigLoaderBuilder
import com.sksamuel.hoplite.PropertySource
import com.sksamuel.hoplite.aws.AwsSecretsManagerPreprocessor
import com.sksamuel.template.myservice.datastore.DatabaseConfig

/**
 * Environment contains the config values specific to the environment we are deployed into.
 */
object Environment {

   // replace 'template-app' with the name of your app
   const val serviceName = "template-app"

   // the ENV env-var is used to determine which configuration files to load
   // we default to local so when running locally we don't need to specify.
   val env = System.getenv("ENV") ?: "local"

   // loads config from each config file in turn, cascading values from top to bottom.
   // that means that values in earlier files take precedence over values in later files.
   val config = ConfigLoaderBuilder.default()
      .addPropertySource(PropertySource.resource("/application-${env}.yml", true))
      .addPropertySource(PropertySource.resource("/reference.yml", true))
      .addPreprocessor(AwsSecretsManagerPreprocessor())
      .report() // shows config values at startup with strings obfuscated for security
      .build()
      .loadConfigOrThrow<Config>()
}

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
   val datadog: DatadogConfig,
   val db: DatabaseConfig,
)
