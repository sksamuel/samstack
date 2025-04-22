dependencies {
   api(projects.templateDomain)
   api(projects.templateDatastore)

   api(libs.bundles.ktor.server)

   testImplementation(testFixtures(projects.templateDatastore))
}
