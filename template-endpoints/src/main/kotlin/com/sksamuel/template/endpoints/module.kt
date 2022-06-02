package com.sksamuel.template.endpoints

import com.sksamuel.template.services.BeerService
import io.ktor.serialization.jackson.jackson
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.plugins.contentnegotiation.ContentNegotiation
import io.ktor.server.routing.routing

/**
 * Organize ktor apps into modules.
 *
 * Each module contains a subset of endpoints and the module can be added to setup tests.
 */
fun Application.module(service: BeerService) {
   // setup json marshalling - provide your own jackson mapper if you have custom jackson modules
   install(ContentNegotiation) { jackson() }
   routing {
      beerEndpoints(service)
   }
}
