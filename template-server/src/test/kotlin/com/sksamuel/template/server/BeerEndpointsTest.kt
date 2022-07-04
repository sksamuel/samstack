package com.sksamuel.template.server

import com.sksamuel.template.datastore.BeerDatastore
import com.sksamuel.template.testkit.postgres
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

      test("GET /v1/beer should return all beers") {
         testApplication {
            application {
               module(service)
            }
            val resp = client.get("/beer")
            resp.status shouldBe HttpStatusCode.OK
         }
      }
   }
}
