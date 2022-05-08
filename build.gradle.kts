buildscript {
   repositories {
      mavenCentral()
      mavenLocal()
   }
}

plugins {
   id("java")
   id("java-library")
   kotlin("jvm")
}

group = "com.sksamuel.template"
version = "0.0.1-SNAPSHOT"

allprojects {
   apply(plugin = "org.jetbrains.kotlin.jvm")

   repositories {
      mavenCentral()
      mavenLocal()
   }

   dependencies {

      // base kotlin libraries
      api(kotlin("stdlib-jdk8"))
      implementation(KotlinX.coroutines.core)
      implementation(KotlinX.coroutines.jdk8)

      // metrics
      implementation("io.micrometer:micrometer-registry-datadog:_")

      // fp
      implementation("com.sksamuel.tabby:tabby-fp:_")

      // logging
      implementation("ch.qos.logback:logback-classic:_")
      implementation("org.slf4j:slf4j-api:_")
      implementation("io.github.microutils:kotlin-logging:_")

      // kotest framework and assertions
      testImplementation(Testing.kotest.framework.datatest)
      testImplementation(Testing.kotest.runner.junit5)
      testImplementation(Testing.kotest.assertions.core)
      testImplementation(Testing.kotest.assertions.json)
      testImplementation(Testing.kotest.property)

      // test containers
      testImplementation("org.testcontainers:mysql:_")
      testImplementation("org.testcontainers:rabbitmq:_")
      testImplementation(Testing.kotestExtensions.testContainers)
      testImplementation(Testing.kotestExtensions.wiremock)

      // ktor testing
      testImplementation(Ktor.server.testHost)
      testImplementation("io.kotest.extensions:kotest-assertions-ktor:_")
   }

   // configure kotest to run
   tasks.test {
      useJUnitPlatform()
      testLogging {
         showExceptions = true
         showStandardStreams = true
         exceptionFormat = org.gradle.api.tasks.testing.logging.TestExceptionFormat.FULL
      }
   }

   // set all projects to latest LTS of the JDK and stable kotlin version
   tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
      kotlinOptions.jvmTarget = "17"
      kotlinOptions.freeCompilerArgs += "-Xopt-in=kotlin.time.ExperimentalTime"
   }
}
