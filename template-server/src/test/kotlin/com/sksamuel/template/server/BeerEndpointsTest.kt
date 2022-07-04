package com.sksamuel.template.server

import com.sksamuel.template.datastore.BeerDatastore
import io.kotest.core.extensions.install
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import io.ktor.client.request.get
import io.ktor.http.HttpStatusCode
import io.ktor.server.testing.testApplication

class BeerEndpointsTest : FunSpec() {
   init {

      val ds = install(postgres)
      val service = BeerService(BeerDatastore(ds))

      test("Missing route should return 404") {
         testApplication {
            application {
               module(service)
            }
            val resp = client.get("/v1/unkn")
            resp.status shouldBe HttpStatusCode.NotFound
         }
      }

      test("GET /v1/beer should return all beers") {
         testApplication {
            application {
               module(service)
            }
            val resp = client.get("/v1/beer")
            resp.status shouldBe HttpStatusCode.OK
         }
      }
   }
}
