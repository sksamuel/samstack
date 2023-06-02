plugins {
   id("com.bmuschko.docker-java-application")
}

dependencies {
   api(projects.templateDomain)
   api(projects.templateDatastore)
   api(projects.templateServices)

   api(rootProject.deps.bundles.hoplite)

   api(rootProject.deps.micrometer.core)
   api(rootProject.deps.micrometer.registry.datadog)

   // health checks and info endpoints
   implementation(rootProject.deps.bundles.cohort)
}

docker {
   javaApplication {
      baseImage.set(rootProject.deps.versions.dockerBaseImage)
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
