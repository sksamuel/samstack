dependencies {
   api(projects.templateDomain)
   api(projects.templateDatastore)

   api(rootProject.deps.bundles.ktor.server)
   api(rootProject.deps.ktor.serialization.jackson)

   testImplementation(rootProject.deps.ktor.server.test.host)
   testImplementation(testFixtures(projects.templateDatastore))
}
