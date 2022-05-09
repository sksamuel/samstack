package com.sksamuel.template.datastore

import com.sksamuel.template.myservice.domain.Beer
import com.sksamuel.template.myservice.domain.BeerName
import com.sksamuel.template.myservice.domain.BeerType
import com.sksamuel.template.myservice.domain.Iso3Country
import org.springframework.jdbc.core.RowMapper
import javax.sql.DataSource

class BeerDatastore(ds: DataSource) {

   private val template = JdbcCoroutineTemplate(ds)

   private val mapper = RowMapper { rs, _ ->
      Beer(
         name = BeerName(rs.getString("name")),
         type = BeerType.valueOf(rs.getString("type")),
         country = Iso3Country(rs.getString("country")),
      )
   }

   suspend fun insert(beer: Beer): Result<Int> {
      return template.update(
         "INSERT INTO beers (name, type, country) VALUES (?,?,?)",
         listOf(beer.name.value, beer.type.name, beer.country.value)
      )
   }

   suspend fun findAll(): Result<List<Beer>> {
      return template.queryForList("SELECT * FROM beers", mapper)
   }
}
