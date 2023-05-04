rootProject.name = "template"

include(
   ":template-app",
   ":template-client",
   ":template-datastore",
   ":template-domain",
   ":template-services",
)

pluginManagement {
   plugins {
      id("com.bmuschko.docker-java-application") version ("9.3.1")
   }
}

enableFeaturePreview("STABLE_CONFIGURATION_CACHE")
enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")
