plugins {
   kotlin("jvm")
}

dependencies {
   api(project(":template-services"))
   implementation(libs.bundles.ktor.server)
}
