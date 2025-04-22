package com.sksamuel.template.myservice.domain

// in kotlin 1.7 we will be to replace these with value classes
data class BeerName(val value: String)
data class Iso3Country(val value: String)

data class Beer(
   val name: BeerName,
   val type: BeerType,
   val country: Iso3Country,
)

enum class BeerType {
   IPA, Belgium, Stout
}
