plugins {
   id("com.bmuschko.docker-java-application")
}

dependencies {
   api(projects.templateDomain)
   api(projects.templateDatastore)
   api(projects.templateServices)

   api("com.sksamuel.hoplite:hoplite-core:2.6.5")
   api("com.sksamuel.hoplite:hoplite-yaml:2.6.5")
   api("com.sksamuel.hoplite:hoplite-aws:2.6.5")
   api("com.sksamuel.hoplite:hoplite-micrometer-datadog:2.6.5")

   api("io.micrometer:micrometer-core:1.10.0")
   api("io.micrometer:micrometer-registry-datadog:1.10.0")

   // health checks and info endpoints
   implementation("com.sksamuel.cohort:cohort-core:1.7.3")
   implementation("com.sksamuel.cohort:cohort-kafka:1.7.3")
   implementation("com.sksamuel.cohort:cohort-hikari:1.7.3")
   implementation("com.sksamuel.cohort:cohort-ktor2:1.7.3")
   implementation("com.sksamuel.cohort:cohort-micrometer:1.7.3")
}

docker {
   javaApplication {
      baseImage.set("amazoncorretto:17.0.5-alpine")
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
