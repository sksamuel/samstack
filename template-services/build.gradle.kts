dependencies {
   api(projects.templateDomain)
   api(projects.templateDatastore)

   val ktor = "2.3.0"
   api("io.ktor:ktor-server-netty:$ktor")
   api("io.ktor:ktor-server-compression:$ktor")
   api("io.ktor:ktor-server-content-negotiation:$ktor")
   api("io.ktor:ktor-server-metrics-micrometer:$ktor")
   api("io.ktor:ktor-server-default-headers:$ktor")
   api("io.ktor:ktor-server-cors:$ktor")
   api("io.ktor:ktor-server-hsts:$ktor")
   api("io.ktor:ktor-serialization-jackson:$ktor")

   testImplementation("io.ktor:ktor-server-test-host:$ktor")
   testImplementation(testFixtures(projects.templateDatastore))
}
