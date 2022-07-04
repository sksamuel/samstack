plugins {
   kotlin("jvm")
}

dependencies {
   api(project(":template-domain"))
   api(project(":template-datastore"))
   api(project(":template-testkit"))
   implementation(libs.bundles.ktor.server)
}
