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

      api(rootProject.libs.bundles.coroutines)
      api(rootProject.libs.bundles.logging)
      api(rootProject.libs.bundles.arrow)
      api(rootProject.libs.tabby.core)

      testImplementation(rootProject.libs.bundles.kotest)
      testImplementation(rootProject.libs.bundles.test.containers)
      testImplementation(rootProject.libs.bundles.mockserver)
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
