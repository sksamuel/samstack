plugins {
   kotlin("jvm")
   application
   id("com.bmuschko.docker-java-application") version "7.2.0"
}

dependencies {
   implementation(project(":template-datastore"))
   implementation(project(":template-services"))
   implementation(project(":template-endpoints"))

   implementation("io.ktor:ktor-server-compression:2.0.0")

   implementation("com.sksamuel.cohort:cohort-core:1.3.0")
   implementation("com.sksamuel.cohort:cohort-ktor2:1.3.0")

   // for data class based configuration
   api("com.sksamuel.hoplite:hoplite-core:2.1.3")
   api("com.sksamuel.hoplite:hoplite-yaml:2.1.3")
   api("com.sksamuel.hoplite:hoplite-aws:2.1.3")
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
