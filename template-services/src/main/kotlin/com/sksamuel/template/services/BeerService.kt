package com.sksamuel.template.services

import com.sksamuel.template.myservice.domain.Beer
import com.sksamuel.template.myservice.domain.BeerName
import com.sksamuel.template.myservice.domain.BeerType

class BeerService {
   fun distill(name: String, type: BeerType): Beer {
      return Beer(BeerName(name), type)
   }
}
