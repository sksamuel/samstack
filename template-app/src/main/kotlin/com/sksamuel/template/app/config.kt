package com.sksamuel.template.app

import com.sksamuel.hoplite.ConfigLoaderBuilder
import com.sksamuel.hoplite.addResourceSource
import com.sksamuel.hoplite.env.Environment
import com.sksamuel.hoplite.secrets.PrefixObfuscator
import com.sksamuel.template.datastore.DatabaseConfig
import io.micrometer.datadog.DatadogConfig
import kotlin.time.Duration

/**
 * Loads config from each config file in turn, cascading values from top to bottom.
 * This means that values in earlier files take precedence over values in later files.
 *
 * @param env value used to specify which environment file(s) to load, eg PROD or STAGING.
 * @return the constructed [Config] object.
 */
fun createConfig(env: Environment) = ConfigLoaderBuilder.default()
   // .addPreprocessor(AwsSecretsManagerPreprocessor(report = true)) // uncomment to enable aws secrets processing
   // .addPreprocessor(GcpSecretManagerPreprocessor(report = true)) // uncomment to enable gcp secrets processing
   // .addPreprocessor(VaultPreprocessor(report = true)) // uncomment to enable hashicorp vault secrets processing
   // .addPreprocessor(AzureKeyVaultPreprocessor(report = true)) // uncomment to enable azure secrets processing
   .addResourceSource("/application-${env}.yml", optional = true) // env specific settings
   .addResourceSource("/shared.yml", optional = true) // shared config goes in shared.yml
   .withReport() // shows config values at startup
   .withObfuscator(PrefixObfuscator(4)) // shows 4 characters from each value in the report, with the rest obfuscated
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
   val server: ServerConfig,
   val datadog: DatadogConfig,
   val db: DatabaseConfig,
)

data class ServerConfig(
   val port: Int,
   val prewait: Duration,
   val grace: Duration,
   val timeout: Duration,
)
