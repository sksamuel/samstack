package com.sksamuel.template.client

import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.HttpRequestRetry
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.compression.ContentEncoding

class BeerClient {

   private val client = HttpClient(CIO) {
      install(ContentEncoding)
      install(HttpTimeout) {
         requestTimeoutMillis = 3000
         connectTimeoutMillis = 2000
         socketTimeoutMillis = 3000
      }
      install(HttpRequestRetry) {
         retryOnServerErrors(maxRetries = 5)
         exponentialDelay()
      }
   }
}
