plugins {
   id("com.bmuschko.docker-java-application")
}

dependencies {
   api(projects.samstackDomain)
   api(projects.samstackDatastore)
   api(projects.samstackServices)

   api(libs.bundles.hoplite)

   api(libs.micrometer.core)
   api(libs.micrometer.datadog)

   // health checks and info endpoints
   api(libs.bundles.cohort)
}

docker {
   javaApplication {
      baseImage.set("amazoncorretto:24.0.1-alpine3.21")
      ports.set(listOf(8080))
      mainClassName.set("com.sksamuel.template.app.MainKt")
      jvmArgs.set(
         listOf(
            "-Djava.security.egd=file:/dev/./urandom",
            // standard JVM flags that use memory settings suitable for containers
            "-XX:+UseContainerSupport",
            "-XX:MaxRAMPercentage=80",
         )
      )
   }
}
