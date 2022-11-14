rootProject.name = "template"

include(
   ":template-app",
   ":template-client",
   ":template-datastore",
   ":template-domain",
   ":template-server",
)

pluginManagement {
   plugins {
      id("com.bmuschko.docker-java-application") version ("9.0.0")
   }
}

enableFeaturePreview("STABLE_CONFIGURATION_CACHE")
enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")
