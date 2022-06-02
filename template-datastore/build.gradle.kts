plugins {
   kotlin("jvm")
}

dependencies {
   api(project(":template-domain"))
   api(libs.micrometer.registry.datadog)
   api(libs.spring.jdbc)
   api(libs.hikaricp)
   api(libs.postgresql)
}
