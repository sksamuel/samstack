plugins {
   kotlin("jvm")
}

dependencies {
   api(project(":template-domain"))
   api(project(":template-datastore"))
   api(Ktor.server.netty)
   api("io.ktor:ktor-serialization-jackson:_")
   api("io.ktor:ktor-server-content-negotiation:_")
   api("io.ktor:ktor-server-metrics-micrometer:_")
}
