package com.sksamuel.template.services

import com.sksamuel.template.datastore.BeerDatastore
import com.sksamuel.template.myservice.domain.Beer
import com.sksamuel.template.myservice.domain.BeerName
import com.sksamuel.template.myservice.domain.BeerType
import com.sksamuel.template.myservice.domain.Iso3Country

class BeerService(private val datastore: BeerDatastore) {

   suspend fun brew(name: String, type: BeerType, country: Iso3Country): Result<Beer> {
      val beer = Beer(BeerName(name), type, country)
      return datastore.insert(beer).map { beer }
   }

   suspend fun all(): Result<List<Beer>> {
      return datastore.findAll()
   }
}
