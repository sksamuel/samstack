package com.sksamuel.template.myservice.services

import com.sksamuel.template.myservice.datastore.ExampleDatastore
import com.sksamuel.template.myservice.domain.ExampleObject
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.call
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import kotlin.random.Random

fun Route.endpoints(datastore: ExampleDatastore) {

   get("/random") {
      call.respond(ExampleObject("foo", Random.nextInt()))
   }

   get("/database") {
      datastore.findAll().fold(
         { call.respond(HttpStatusCode.OK, it) },
         { call.respond(HttpStatusCode.InternalServerError, it) },
      )
   }
}
