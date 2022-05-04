buildscript {
   repositories {
      mavenCentral()
      mavenLocal()
   }
}

plugins {
   id("java")
   id("java-library")
   kotlin("jvm").version("1.6.21")
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
      implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.1")
      implementation("org.jetbrains.kotlinx:kotlinx-coroutines-jdk8:1.6.1")

      // metrics
      implementation("io.micrometer:micrometer-registry-datadog:1.8.5")

      // fp
      implementation("com.sksamuel.tabby:tabby-fp:2.0.17")

      // logging
      implementation("ch.qos.logback:logback-classic:1.2.11")
      implementation("org.slf4j:slf4j-api:1.7.36")
      implementation("io.github.microutils:kotlin-logging:2.1.21")

      // kotest framework and assertions
      testImplementation("io.kotest:kotest-framework-datatest:5.3.0")
      testImplementation("io.kotest:kotest-runner-junit5:5.3.0")
      testImplementation("io.kotest:kotest-assertions-core:5.3.0")
      testImplementation("io.kotest:kotest-assertions-json:5.3.0")
      testImplementation("io.kotest:kotest-property:5.3.0")

      // test containers
      testImplementation("org.testcontainers:mysql:1.17.1")
      testImplementation("org.testcontainers:rabbitmq:1.17.1")
      testImplementation("io.kotest.extensions:kotest-extensions-testcontainers:1.3.3")
      testImplementation("io.kotest.extensions:kotest-extensions-wiremock:1.0.3")

      // ktor testing
      testImplementation("io.ktor:ktor-server-test-host:2.0.0")
      testImplementation("io.kotest.extensions:kotest-assertions-ktor:1.0.3")
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
