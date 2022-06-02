buildscript {
   repositories {
      mavenCentral()
      mavenLocal()
   }
}

plugins {
   kotlin("jvm").version("1.6.21")
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

      // base kotlin libraries
      api(kotlin("stdlib-jdk8"))
//      implementation(libs.kotlinx.coroutines.core)
//      implementation(libs.kotlinx.coroutines.jdk8)

      // metrics
//

      // fp
//      implementation("com.sksamuel.tabby:tabby-fp:_")

      // logging


//       test containers
//      testImplementation("org.testcontainers:mysql:_")
//      testImplementation("org.testcontainers:rabbitmq:_")
//      testImplementation(libs.kotest.extensions.testcontainers)
//      testImplementation(libs.kotest.extensions.mockserver)
//
//       ktor testing
//      testImplementation(libs.ktor.server.test.host)
//      testImplementation("io.kotest.extensions:kotest-assertions-ktor:_")
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
