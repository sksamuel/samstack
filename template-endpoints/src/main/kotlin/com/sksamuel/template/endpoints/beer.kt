package com.sksamuel.template.endpoints

import com.sksamuel.template.services.BeerService
import io.ktor.server.routing.Route
import io.ktor.server.routing.get

fun Route.beerEndpoints(service: BeerService) {
   get("/beer") {
      service.all().fold(
         {},
         {}
      )
   }
}
