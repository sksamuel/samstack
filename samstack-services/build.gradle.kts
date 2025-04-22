dependencies {
   api(projects.samstackDomain)
   api(projects.samstackDatastore)

   api(libs.bundles.ktor.server)

   testImplementation(testFixtures(projects.samstackDatastore))
}
