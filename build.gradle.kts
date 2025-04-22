import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.dsl.KotlinVersion
import org.jetbrains.kotlin.gradle.tasks.KotlinJvmCompile

plugins {
   kotlin("jvm") version "2.1.20"
}

subprojects {
   apply(plugin = "org.jetbrains.kotlin.jvm")
   dependencies {
      with(rootProject) {
         implementation(libs.bundles.coroutines)
         implementation(libs.bundles.logging)
         implementation(libs.tabby)
         testImplementation(libs.bundles.testing)
      }
   }
   tasks {
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
            apiVersion.set(KotlinVersion.KOTLIN_2_1)
            languageVersion.set(KotlinVersion.KOTLIN_2_1)
            jvmTarget.set(JvmTarget.JVM_17)
         }
      }
   }
}
