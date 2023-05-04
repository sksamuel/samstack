dependencies {
   api(projects.templateDomain)
   val ktor = "2.3.0"
   api("io.ktor:ktor-client-apache:$ktor")
   implementation("io.ktor:ktor-client-encoding:$ktor")
}
