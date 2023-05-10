buildscript {
   repositories {
      mavenCentral()
      mavenLocal()
   }
}

plugins {
   kotlin("jvm").version("1.8.21")
}

group = "com.sksamuel.template"
version = "0.0.1-SNAPSHOT"

allprojects {
   repositories {
      mavenCentral()
      mavenLocal()
   }
}

subprojects {
   apply(plugin = "org.jetbrains.kotlin.jvm")

   dependencies {

      implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.4")
      implementation("org.jetbrains.kotlinx:kotlinx-coroutines-jdk8:1.6.4")

      implementation("io.github.microutils:kotlin-logging:3.0.5")
      implementation("ch.qos.logback:logback-classic:1.4.4")
      implementation("org.slf4j:slf4j-api:2.0.7")

      implementation("com.sksamuel.tabby:tabby-fp:2.2.3")

      testImplementation("io.kotest:kotest-framework-datatest:5.6.1")
      testImplementation("io.kotest:kotest-runner-junit5:5.6.1")
      testImplementation("io.kotest:kotest-assertions-core:5.6.1")
      testImplementation("io.kotest:kotest-property:5.6.1")
      testImplementation("io.kotest.extensions:kotest-extensions-testcontainers:1.3.4")
      testImplementation("io.kotest.extensions:kotest-extensions-httpstub:2.0.0")
      testImplementation("org.testcontainers:postgresql:1.18.0")
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
      kotlinOptions {
         jvmTarget = "17"
         apiVersion = "1.8"
         languageVersion = "1.8"
      }
   }
}
