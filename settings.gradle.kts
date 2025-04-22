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
      id("com.bmuschko.docker-java-application") version ("9.4.0")
   }
}

enableFeaturePreview("STABLE_CONFIGURATION_CACHE")
enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

dependencyResolutionManagement {
   repositoriesMode = RepositoriesMode.FAIL_ON_PROJECT_REPOS
   repositories {
      mavenCentral()
      mavenLocal()
      maven("https://oss.sonatype.org/content/repositories/snapshots")
      maven("https://s01.oss.sonatype.org/content/repositories/snapshots")
   }
   versionCatalogs {
      create("libs") {

         val micrometer = "1.14.5"
         library("micrometer-core", "io.micrometer:micrometer-core:$micrometer")
         library("micrometer-datadog", "io.micrometer:micrometer-registry-datadog:$micrometer")

         library("kotlin-logging", "io.github.oshai:kotlin-logging-jvm:7.0.6")

         library("coroutines-core", "org.jetbrains.kotlinx:kotlinx-coroutines-core:1.10.2")

         val vertx = "4.5.14"
         library("vertx-core", "io.vertx:vertx-core:$vertx")
         library("vertx-web", "io.vertx:vertx-web:$vertx")
         library("vertx-web-client", "io.vertx:vertx-web-client:$vertx")
         library("vertx-kotlin", "io.vertx:vertx-lang-kotlin:$vertx")
         library("vertx-coroutines", "io.vertx:vertx-lang-kotlin-coroutines:$vertx")
         library("vertx-micrometer", "io.vertx:vertx-micrometer-metrics:$vertx")
         library("vertx-auth-common", "io.vertx:vertx-auth-common:$vertx")
         library("vertx-circuit-breaker", "io.vertx:vertx-circuit-breaker:$vertx")
         library("netty-transport-native-epoll", "io.netty:netty-transport-native-epoll:4.1.108.Final")

         library("tabby", "com.sksamuel.tabby:tabby-fp:2.2.11")
         library("tribune-core", "com.sksamuel.tribune:tribune-core:2.0.1")

         val cohort = "2.7.0"
         library("cohort-ktor", "com.sksamuel.cohort:cohort-ktor:$cohort")
         library("cohort-vertx", "com.sksamuel.cohort:cohort-vertx:$cohort")
         library("cohort-micrometer", "com.sksamuel.cohort:cohort-micrometer:$cohort")
         library("cohort-hikari", "com.sksamuel.cohort:cohort-hikari:$cohort")
         library("cohort-lettuce", "com.sksamuel.cohort:cohort-lettuce:$cohort")
         library("cohort-kafka", "com.sksamuel.cohort:cohort-kafka:$cohort")
         library("cohort-log4j2", "com.sksamuel.cohort:cohort-log4j2:$cohort")

         val hoplite = "2.9.0"
         library("hoplite-core", "com.sksamuel.hoplite:hoplite-core:$hoplite")
         library("hoplite-aws", "com.sksamuel.hoplite:hoplite-aws:$hoplite")
         library("hoplite-yaml", "com.sksamuel.hoplite:hoplite-yaml:$hoplite")
         library("hoplite-micrometer-datadog", "com.sksamuel.hoplite:hoplite-micrometer-datadog:$hoplite")

         library("sksamuel-centurion-avro", "com.sksamuel.centurion:centurion-avro:1.4.0")

         val ktor = "2.3.12"
         library("ktor-server-core", "io.ktor:ktor-server-core:$ktor")
         library("ktor-server-auth", "io.ktor:ktor-server-auth:$ktor")
         library("ktor-server-netty", "io.ktor:ktor-server-netty:$ktor")
         library("ktor-server-cors", "io.ktor:ktor-server-cors:$ktor")
         library("ktor-server-compression", "io.ktor:ktor-server-compression:$ktor")
         library("ktor-server-metrics-micrometer", "io.ktor:ktor-server-metrics-micrometer:$ktor")
         library("ktor-server-content-negotiation", "io.ktor:ktor-server-content-negotiation:$ktor")
         library("ktor-server-conditional-headers", "io.ktor:ktor-server-conditional-headers:$ktor")
         library("ktor-server-test-host", "io.ktor:ktor-server-test-host:$ktor")

         library("ktor-serialization-jackson", "io.ktor:ktor-serialization-jackson:$ktor")

         library("ktor-client-core", "io.ktor:ktor-client-core:$ktor")
         library("ktor-client-apache5", "io.ktor:ktor-client-apache5:$ktor")
         library("ktor-client-content-negotiation", "io.ktor:ktor-client-content-negotiation:$ktor")
         library("ktor-client-encoding", "io.ktor:ktor-client-encoding:$ktor")

         library("spatial4j", "org.locationtech.spatial4j:spatial4j:0.8")

         val lettuce = "6.4.2.RELEASE"
         library("lettuce-core", "io.lettuce:lettuce-core:$lettuce")

         val springJdbc = "6.2.5"
         library("spring-jdbc", "org.springframework:spring-jdbc:$springJdbc")

         val hikari = "6.3.0"
         library("hikari", "com.zaxxer:HikariCP:$hikari")

         val postgres = "42.7.5"
         library("postgresql", "org.postgresql:postgresql:$postgres")

         val flyway = "9.22.3"
         library("flyway-core", "org.flywaydb:flyway-core:$flyway")

         val kotest = "6.0.0.M3"
         library("kotest-junit5", "io.kotest:kotest-runner-junit5:$kotest")
         library("kotest-core", "io.kotest:kotest-assertions-core:$kotest")
         library("kotest-json", "io.kotest:kotest-assertions-json:$kotest")
         library("kotest-property", "io.kotest:kotest-property:$kotest")
         library("kotest-ktor", "io.kotest:kotest-assertions-ktor:$kotest")
         library("kotest-testcontainers", "io.kotest:kotest-extensions-testcontainers:$kotest")

         val testcontainers = "1.20.6"
         library("testcontainers", "org.testcontainers:testcontainers:$testcontainers")
         library("testcontainers-postgresql", "org.testcontainers:postgresql:$testcontainers")
         library("testcontainers-mysql", "org.testcontainers:mysql:$testcontainers")
         library("testcontainers-localstack", "org.testcontainers:localstack:$testcontainers")
         library("testcontainers-redis", "com.redis:testcontainers-redis:2.2.4")

         val jackson = "2.18.3"
         library("jackson-core", "com.fasterxml.jackson.core:jackson-core:$jackson")
         library("jackson-databind", "com.fasterxml.jackson.core:jackson-databind:$jackson")
         library("jackson-dataformat-yaml", "com.fasterxml.jackson.dataformat:jackson-dataformat-yaml:$jackson")
         library("jackson-module-kotlin", "com.fasterxml.jackson.module:jackson-module-kotlin:$jackson")
         library("jackson-datatype-jsr", "com.fasterxml.jackson.datatype:jackson-datatype-jsr310:$jackson")
         library("jackson-annotations", "com.fasterxml.jackson.core:jackson-annotations:$jackson")

         val slf4j = "2.0.17"
         library("slf4j-api", "org.slf4j:slf4j-api:$slf4j")
         library("log4j-over-slf4j", "org.slf4j:log4j-over-slf4j:$slf4j")

         val log4j = "2.24.0"
         library("log4j-slf4j2-impl", "org.apache.logging.log4j:log4j-slf4j2-impl:$log4j")
         library("log4j-layout-template-json", "org.apache.logging.log4j:log4j-layout-template-json:$log4j")

         val aws1 = "1.12.782"
         library("aws-java-sdk-sts", "com.amazonaws:aws-java-sdk-sts:$aws1")
         library("aws-java-sdk-s3", "com.amazonaws:aws-java-sdk-s3:$aws1")
         library("aws-java-sdk-kms", "com.amazonaws:aws-java-sdk-kms:$aws1")

         val aws2 = "2.31.19"
         library("aws-sdk2-dynamodb", "software.amazon.awssdk:dynamodb:$aws2")
         library("aws-sdk2-s3", "software.amazon.awssdk:s3:$aws2")
         library("aws-sdk2-sts", "software.amazon.awssdk:sts:$aws2")
         library("aws-sdk2-codeartifact", "software.amazon.awssdk:codeartifact:$aws2")

         library("aws-jdbc", "software.amazon.jdbc:aws-advanced-jdbc-wrapper:2.5.6")

         bundle(
            "coroutines",
            listOf(
               "coroutines-core",
            )
         )

         bundle(
            "ktor-client",
            listOf(
               "ktor-client-core",
               "ktor-client-apache5",
               "ktor-client-content-negotiation",
               "ktor-client-encoding",
               "ktor-serialization-jackson",
            )
         )
         bundle(
            "jackson",
            listOf(
               "jackson-core",
               "jackson-databind",
               "jackson-dataformat-yaml",
               "jackson-module-kotlin",
               "jackson-datatype-jsr",
               "jackson-annotations",
            )
         )
         bundle(
            "hoplite",
            listOf(
               "hoplite-core",
               "hoplite-aws",
               "hoplite-yaml",
               "hoplite-micrometer-datadog",
               "aws-java-sdk-sts",
               "aws-sdk2-sts",
            )
         )
         bundle(
            "cohort",
            listOf(
               "cohort-hikari",
               "cohort-kafka",
               "cohort-ktor",
               "cohort-lettuce",
               "cohort-log4j2",
               "cohort-micrometer",
               "cohort-vertx",
            )
         )
         bundle(
            "ktor-server",
            listOf(
               "ktor-server-core",
               "ktor-server-netty",
               "ktor-server-auth",
               "ktor-server-content-negotiation",
               "ktor-server-conditional-headers",
               "ktor-server-metrics-micrometer",
               "ktor-server-compression",
               "ktor-server-cors",
               "ktor-serialization-jackson",
            )
         )
         bundle(
            "logging",
            listOf(
               "log4j-over-slf4j",
               "log4j-layout-template-json",
               "log4j-slf4j2-impl",
               "jackson-dataformat-yaml",
               "jackson-databind"
            )
         )
         bundle(
            "testing",
            listOf(
               "log4j-slf4j2-impl",
               "kotest-junit5",
               "kotest-core",
               "kotest-json",
               "kotest-property",
               "kotest-ktor",
               "kotest-testcontainers",
               "testcontainers",
               "testcontainers-postgresql",
               "testcontainers-mysql",
               "testcontainers-localstack",
               "ktor-server-test-host",
               "ktor-client-content-negotiation",
               "ktor-serialization-jackson",
            )
         )
      }
   }
}

