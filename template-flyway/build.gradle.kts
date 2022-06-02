plugins {
   kotlin("jvm")
   id("com.bmuschko.docker-java-application").version("7.3.0")
}

dependencies {
   api(project(":template-datastore"))
   api(libs.flyway.core)

   // for data class based configuration
   api(libs.bundles.hoplite)
   api(libs.bundles.logging)

   testImplementation(libs.bundles.kotest)
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
