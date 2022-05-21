plugins {
   kotlin("jvm")
   id("com.bmuschko.docker-java-application")
}

dependencies {
   api(project(":template-datastore"))
   api("org.flywaydb:flyway-core:_")

   // for data class based configuration
   api("com.sksamuel.hoplite:hoplite-core:_")
   api("com.sksamuel.hoplite:hoplite-yaml:_")
   api("com.sksamuel.hoplite:hoplite-aws:_")
}

docker {
   javaApplication {
      baseImage.set("openjdk:17-slim")
      ports.set(listOf(8080))
      mainClassName.set("com.sksamuel.template.flyway.MainKt")
      // standard JVM flags that set memory suitable for containers
      jvmArgs.set(
         listOf(
            "-Djava.security.egd=file:/dev/./urandom",
            "-XX:+UseContainerSupport",
            "-XX:MaxRAMPercentage=80",
         )
      )
   }
}
