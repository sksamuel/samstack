package com.sksamuel.template.testkit

import com.sksamuel.template.myservice.domain.Iso3Country
import io.kotest.property.Arb
import io.kotest.property.arbitrary.map
import io.kotest.property.arbitrary.string

fun Arb.Companion.iso3Country() = Arb.string(3).map { Iso3Country(it) }
