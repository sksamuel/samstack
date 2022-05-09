package com.sksamuel.template.myservice.datastore

import com.sksamuel.template.myservice.domain.Beer
import com.sksamuel.template.myservice.domain.BeerName
import com.sksamuel.template.myservice.domain.BeerType
import org.springframework.jdbc.core.RowMapper
import javax.sql.DataSource

class BeerDatastore(ds: DataSource) {

   private val template = JdbcCoroutineTemplate(ds)

   private val mapper = RowMapper { rs, _ ->
      Beer(
         name = BeerName(rs.getString("name")),
         type = BeerType.valueOf(rs.getString("type")),
      )
   }

   suspend fun findAll(): Result<List<Beer>> {
      return template.queryForList("SELECT * FROM beers", mapper)
   }
}
