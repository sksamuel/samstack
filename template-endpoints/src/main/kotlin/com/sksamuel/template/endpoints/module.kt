package com.sksamuel.template.endpoints

import io.ktor.serialization.jackson.jackson
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.plugins.contentnegotiation.ContentNegotiation
import io.ktor.server.routing.IgnoreTrailingSlash
import io.ktor.server.routing.routing

fun Application.module() {
   install(ContentNegotiation) { jackson() }
   install(IgnoreTrailingSlash)
   routing {
      // register your endpoints here
   }
}
