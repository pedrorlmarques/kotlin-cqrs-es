package io.waketunes

import org.springframework.boot.fromApplication
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.boot.testcontainers.service.connection.ServiceConnection
import org.springframework.boot.with
import org.springframework.context.annotation.Bean
import org.springframework.test.context.DynamicPropertyRegistry
import org.testcontainers.containers.KafkaContainer
import org.testcontainers.containers.MongoDBContainer
import org.testcontainers.utility.DockerImageName


@TestConfiguration(proxyBeanMethods = false)
class TestTimelineCommandServiceApplication {

    @Bean
    @ServiceConnection
    fun mongoDbContainer(): MongoDBContainer {
        return MongoDBContainer(DockerImageName.parse("mongo:latest"))
    }

    @Bean
    @ServiceConnection
    fun kafkaContainer(properties: DynamicPropertyRegistry): KafkaContainer {
        val kafkaContainer = KafkaContainer(DockerImageName.parse("confluentinc/cp-kafka:latest"))
        //set the bootstrap server for spring cloud stream
        properties.add("spring.cloud.stream.kafka.binder.brokers", kafkaContainer::getBootstrapServers)
        return kafkaContainer
    }
}

fun main(args: Array<String>) {
    fromApplication<TimelineCommandServiceApplication>().with(TestTimelineCommandServiceApplication::class).run(*args)
}
