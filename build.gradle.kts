buildscript {
   repositories {
      mavenCentral()
      mavenLocal()
   }
}

plugins {
   alias(deps.plugins.kotlin)
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
   apply(plugin = rootProject.deps.plugins.kotlin.get().pluginId)
   dependencies {
      with(rootProject) {
         implementation(deps.bundles.kotlinx.coroutines)
         implementation(deps.bundles.logging)

         implementation(deps.tabby)

         testImplementation(deps.bundles.kotest)
         testImplementation(deps.bundles.testcontainers)
      }
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
