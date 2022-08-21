plugins {
   kotlin("jvm")
   id("com.bmuschko.docker-java-application").version("8.0.0")
}

dependencies {
   api(project(":template-datastore"))
   api(project(":template-server"))

   api(libs.micrometer.registry.datadog)

   api(libs.bundles.ktor.server)
   api(libs.bundles.cohort)
   api(libs.bundles.hoplite)
}

docker {
   javaApplication {
      baseImage.set("eclipse-temurin:17-alpine")
      ports.set(listOf(8080))
      mainClassName.set("com.sksamuel.template.app.MainKt")
      // standard JVM flags that use memory settings suitable for containers
      jvmArgs.set(
         listOf(
            "-Djava.security.egd=file:/dev/./urandom",
            "-XX:+UseContainerSupport",
            "-XX:MaxRAMPercentage=80",
         )
      )
   }
}
