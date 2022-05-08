plugins {
   kotlin("jvm")
}

dependencies {
   api(project(":template-domain"))
   api("org.springframework:spring-jdbc:_")
   api("org.postgresql:postgresql:_")
   api("com.zaxxer:HikariCP:_")
}
