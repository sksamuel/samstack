plugins {
   `java-test-fixtures`
}

dependencies {
   api(projects.templateDomain)
   api(rootProject.deps.micrometer.core)
   api(rootProject.deps.postgresql)
   api(rootProject.deps.hikari)
   api(rootProject.deps.spring.jdbc)
   api(rootProject.deps.flyway.core)
}

dependencies {
   testFixturesImplementation(rootProject.deps.kotest.extensions.testcontainers)
   testFixturesImplementation(rootProject.deps.testcontainers.postgresql)
}
