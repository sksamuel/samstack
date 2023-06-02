plugins {
   `java-test-fixtures`
}

dependencies {
   api(rootProject.deps.bundles.jackson)
}

dependencies {
   testFixturesImplementation(rootProject.deps.kotest.property)
}
