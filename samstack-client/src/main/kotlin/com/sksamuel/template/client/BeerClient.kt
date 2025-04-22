package com.sksamuel.template.client

import com.sksamuel.template.myservice.domain.Beer
import com.sksamuel.template.myservice.domain.JacksonSupport.fromJson
import io.ktor.client.HttpClient
import io.ktor.client.engine.apache5.Apache5
import io.ktor.client.plugins.HttpRequestRetry
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.compression.ContentEncoding
import io.ktor.client.request.get
import io.ktor.client.statement.readBytes

class BeerClient(
   private val config: ClientConfig,
) {

   private val client = HttpClient(Apache5) {
      install(ContentEncoding)
      install(HttpTimeout) {
         requestTimeoutMillis = config.requestTimeoutMillis
         connectTimeoutMillis = config.connectTimeoutMillis
         socketTimeoutMillis = config.socketTimeoutMillis
      }
      install(HttpRequestRetry) {
         retryOnServerErrors(maxRetries = config.maxRetries)
         exponentialDelay()
      }
      expectSuccess = false
      followRedirects = true
      developmentMode = true
   }

   /**
    * Returns all beers in the system.
    */
   suspend fun getBeers(): Result<List<Beer>> = runCatching {
      val resp = client.get("/beer")
      resp.readBytes().fromJson<List<Beer>>()
   }
}
