package com.sksamuel.template.datastore

import org.testcontainers.containers.PostgreSQLContainer
import org.testcontainers.utility.DockerImageName

private val DOCKER_IMAGE_NAME = DockerImageName.parse("postgres")
val postgresContainer = PostgreSQLContainer(DOCKER_IMAGE_NAME)
