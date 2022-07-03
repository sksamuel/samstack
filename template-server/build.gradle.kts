plugins {
   kotlin("jvm")
}

dependencies {
   api(project(":template-domain"))
   api(project(":template-datastore"))
   implementation(libs.bundles.ktor.server)
}
