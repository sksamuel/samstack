plugins {
   kotlin("jvm")
}

dependencies {
   api(project(":template-domain"))
   implementation(Ktor.client.cio)
   implementation(Ktor.client.encoding)
}
