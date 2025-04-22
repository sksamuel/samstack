package com.sksamuel.template.myservice.domain

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue

object JacksonSupport {

   val mapper = jacksonObjectMapper().apply {
      configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
      configure(DeserializationFeature.READ_DATE_TIMESTAMPS_AS_NANOSECONDS, false)
      configure(SerializationFeature.WRITE_DATE_TIMESTAMPS_AS_NANOSECONDS, false)
      configure(SerializationFeature.WRITE_ENUMS_USING_INDEX, false)
      registerModule(JavaTimeModule())
   }

   inline fun <reified T> String.fromJson() = mapper.readValue<T>(this)
   inline fun <reified T> ByteArray.fromJson() = mapper.readValue<T>(this)
   fun Any.toJson() = mapper.writeValueAsString(this)
}
