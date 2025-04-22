package com.sksamuel.template.client

data class ClientConfig(
   val requestTimeoutMillis: Long,
   val connectTimeoutMillis: Long,
   val socketTimeoutMillis: Long,
   val maxRetries: Int,
)
