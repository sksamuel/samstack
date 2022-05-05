plugins {
   kotlin("jvm")
}

dependencies {
   api(project(":template-services"))
   api("io.ktor:ktor-server-netty:2.0.0")
   api("io.ktor:ktor-serialization-jackson:2.0.0")
   api("io.ktor:ktor-server-content-negotiation:2.0.0")
   api("io.ktor:ktor-server-metrics-micrometer:2.0.0")
}
