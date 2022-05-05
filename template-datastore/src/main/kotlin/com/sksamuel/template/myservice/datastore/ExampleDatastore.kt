package com.sksamuel.template.myservice.datastore

import com.sksamuel.template.myservice.domain.ExampleObject
import org.springframework.jdbc.core.RowMapper
import javax.sql.DataSource

class ExampleDatastore(ds: DataSource) {

   private val template = JdbcCoroutineTemplate(ds)

   private val mapper = RowMapper { rs, _ ->
      ExampleObject(
         a = rs.getString("a"),
         b = rs.getInt("b"),
      )
   }

   suspend fun findAll(): Result<List<ExampleObject>> {
      return template.queryForList("SELECT * FROM example_table", mapper)
   }
}
