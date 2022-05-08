package com.sksamuel.template.client

import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.compression.ContentEncoding

class MyClient {
   private val client = HttpClient(CIO) {
      install(ContentEncoding)
   }
}
