plugins {
   kotlin("jvm")
}

dependencies {
   api(project(":template-domain"))
   api("org.springframework:spring-jdbc:5.3.19")
   api("org.postgresql:postgresql:42.3.4")
   api("com.zaxxer:HikariCP:5.0.1")
}
