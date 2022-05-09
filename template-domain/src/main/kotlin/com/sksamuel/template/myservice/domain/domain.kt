package com.sksamuel.template.myservice.domain

// in kotlin 1.7 we can replace this with a value class
data class BeerName(val name: String)

data class Beer(
   val name: BeerName,
   val type: BeerType,
)

enum class BeerType {
   IPA, Belgium, Stout
}
