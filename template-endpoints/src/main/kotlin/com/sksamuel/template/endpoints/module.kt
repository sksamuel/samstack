package com.sksamuel.template.endpoints

import com.sksamuel.template.services.BeerService
import io.ktor.serialization.jackson.jackson
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.plugins.compression.Compression
import io.ktor.server.plugins.contentnegotiation.ContentNegotiation
import io.ktor.server.plugins.cors.routing.CORS
import io.ktor.server.plugins.defaultheaders.DefaultHeaders
import io.ktor.server.plugins.hsts.HSTS
import io.ktor.server.routing.IgnoreTrailingSlash
import io.ktor.server.routing.routing
import kotlin.time.Duration.Companion.hours

/**
 * Configures the http settings for a ktor server.
 * A module contains routes and the plugins needed for tests.
 */
fun Application.module(service: BeerService) {

   // adds server and date headers
   install(DefaultHeaders)

   // enables zip and deflate compression
   install(Compression)

   install(CORS)

   // enables strict security headers to force TLS
   install(HSTS) { maxAgeInSeconds = 1.hours.inWholeSeconds }

   // setup json marshalling - provide your own jackson mapper if you have custom jackson modules
   install(ContentNegotiation) { jackson() }

   // allows foo/ and foo to be treated the same
   install(IgnoreTrailingSlash)

   routing {
      beerEndpoints(service)
   }
}
