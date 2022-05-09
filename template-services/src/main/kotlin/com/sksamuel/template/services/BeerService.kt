package com.sksamuel.template.services

import com.sksamuel.template.myservice.datastore.BeerDatastore
import com.sksamuel.template.myservice.domain.Beer
import com.sksamuel.template.myservice.domain.BeerName
import com.sksamuel.template.myservice.domain.BeerType

class BeerService(private val datastore: BeerDatastore) {

   fun distill(name: String, type: BeerType): Beer {
      return Beer(BeerName(name), type)
   }

   suspend fun all(): Result<List<Beer>> {
      return datastore.findAll()
   }
}
