package com.sksamuel.template.server

import io.ktor.serialization.jackson.jackson
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.plugins.contentnegotiation.ContentNegotiation
import io.ktor.server.routing.routing

/**
 * Composes settings and endpoints for a ktor service.
 *
 * Each module can bring in multiple endpoints defined in separate files to keep things clean.
 *
 * A module should only contain server settings required for tests - such as json marshalling.
 * Settings that are only useful when deployed, eg health checks or compression, should be defined in the app module.
 *
 * A module can be re-used for tests by passing in the appropriate dependencies.
 */
fun Application.module(service: BeerService) {

   // setup json marshalling - provide your own jackson mapper if you have custom jackson modules
   install(ContentNegotiation) { jackson() }

   routing {
      beerEndpoints(service)
   }
}
