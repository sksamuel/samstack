plugins {
   kotlin("jvm")
}

dependencies {
   api(project(":template-domain"))
   implementation("io.ktor:ktor-client-cio:2.0.1")
   implementation("io.ktor:ktor-client-encoding:2.0.1")
}
