buildscript {
   repositories {
      mavenCentral()
      mavenLocal()
   }
}

plugins {
   kotlin("jvm").version("1.7.20")
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

      implementation("io.github.microutils:kotlin-logging:3.0.3")
      implementation("ch.qos.logback:logback-classic:1.4.4")
      implementation("org.slf4j:slf4j-api:2.0.3")

      implementation("com.sksamuel.tabby:tabby-fp-jvm:2.0.36")

      testImplementation("io.kotest:kotest-framework-datatest:5.5.4")
      testImplementation("io.kotest:kotest-runner-junit5:5.5.4")
      testImplementation("io.kotest:kotest-assertions-core:5.5.4")
      testImplementation("io.kotest:kotest-property:5.5.4")
      testImplementation("io.kotest.extensions:kotest-extensions-testcontainers:1.3.4")
      testImplementation("io.kotest.extensions:kotest-extensions-httpstub:1.0.0")
      testImplementation("org.testcontainers:postgresql:1.17.4")
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
   }
}
