plugins {
   kotlin("jvm")
   id("com.bmuschko.docker-java-application").version("7.3.0")
}

dependencies {
   api(project(":template-datastore"))
   api(project(":template-services"))
   api(project(":template-endpoints"))

   api(libs.micrometer.registry.datadog)

   api(libs.bundles.ktor.server)
   api(libs.bundles.cohort)
   api(libs.bundles.arrow)
   api(libs.bundles.hoplite)
   api(libs.bundles.logging)
}

docker {
   javaApplication {
      baseImage.set("openjdk:17-slim")
      ports.set(listOf(8080))
      mainClassName.set("com.sksamuel.template.app.MainKt")
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
