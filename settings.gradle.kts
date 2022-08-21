include("template-app")
include("template-client")
include("template-datastore")
include("template-domain")
include("template-server")
include("template-testkit")

pluginManagement {
   plugins {
      id("com.bmuschko.docker-java-application") version ("8.0.0")
   }
}
