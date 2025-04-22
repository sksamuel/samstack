plugins {
   id("com.bmuschko.docker-java-application")
}

dependencies {
   api(projects.templateDomain)
   api(projects.templateDatastore)
   api(projects.templateServices)

   api(libs.bundles.hoplite)

   api(libs.micrometer.core)
   api(libs.micrometer.datadog)

   // health checks and info endpoints
   api(libs.bundles.cohort)
}

docker {
   javaApplication {
      baseImage.set("amazoncorretto:17.0.5-alpine")
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
