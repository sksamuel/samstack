package com.sksamuel.template.endpoints

import io.kotest.core.spec.style.StringSpec

class BangExample : StringSpec({
   "!test 1" {
      // this will be skipped
   }

   "test 2" {
      // this will be executed
   }

   "!test 3" {
      // this will be skipped
   }
})
