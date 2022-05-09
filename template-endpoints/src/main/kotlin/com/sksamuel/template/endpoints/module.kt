package com.sksamuel.template.endpoints

import io.ktor.serialization.jackson.jackson
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.plugins.contentnegotiation.ContentNegotiation
import io.ktor.server.routing.routing

fun Application.module() {
   // setsup json marshalling
   // provide your own jackson mapper if you have custom jackson modules
   install(ContentNegotiation) { jackson() }
   routing {
      // register your endpoints here
   }
}
