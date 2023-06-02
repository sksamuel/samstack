import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.dsl.KotlinVersion
import org.jetbrains.kotlin.gradle.tasks.KotlinJvmCompile

plugins {
   alias(deps.plugins.kotlin)
}

allprojects {
   repositories {
      mavenCentral()
      mavenLocal()
   }
}

buildscript {
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
   tasks {
      // configure kotest to run
      test {
         useJUnitPlatform()
         testLogging {
            showExceptions = true
            showStandardStreams = true
            exceptionFormat = org.gradle.api.tasks.testing.logging.TestExceptionFormat.FULL
         }
      }
      withType<KotlinJvmCompile> {
         compilerOptions {
            apiVersion.set(KotlinVersion.KOTLIN_1_8)
            languageVersion.set(KotlinVersion.KOTLIN_1_8)
            jvmTarget.set(JvmTarget.JVM_17)
         }
      }
   }
}
