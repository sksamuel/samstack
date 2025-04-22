plugins {
   `java-test-fixtures`
}

dependencies {
   api(projects.samstackDomain)
   api(libs.micrometer.core)
   api(libs.postgresql)
   api(libs.hikari)
   api(libs.spring.jdbc)
   api(libs.flyway.core)
}
