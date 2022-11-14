dependencies {
   api(projects.templateDomain)
   api(projects.templateDatastore)

   api("io.ktor:ktor-server-netty:2.1.3")
   api("io.ktor:ktor-server-compression:2.1.3")
   api("io.ktor:ktor-server-content-negotiation:2.1.3")
   api("io.ktor:ktor-server-metrics-micrometer:2.1.3")
   api("io.ktor:ktor-server-default-headers:2.1.3")
   api("io.ktor:ktor-server-cors:2.1.3")
   api("io.ktor:ktor-server-hsts:2.1.3")
   api("io.ktor:ktor-serialization-jackson:2.1.3")

   testImplementation("io.ktor:ktor-server-test-host:2.1.3")
   testImplementation(testFixtures(projects.templateDatastore))
}
