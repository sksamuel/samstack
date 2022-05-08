plugins {
   kotlin("jvm")
}

dependencies {
   api(project(":template-services"))
   api(Ktor.server.netty)
   api("io.ktor:ktor-serialization-jackson:_")
   api("io.ktor:ktor-server-content-negotiation:_")
   api("io.ktor:ktor-server-metrics-micrometer:_")
}
