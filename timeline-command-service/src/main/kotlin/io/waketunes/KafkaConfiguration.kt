package io.waketunes

import org.springframework.cloud.stream.binder.DefaultBinderFactory
import org.springframework.cloud.stream.binder.kafka.KafkaMessageChannelBinder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.kafka.transaction.KafkaTransactionManager
import org.springframework.messaging.MessageChannel


@Configuration
class KafkaConfiguration {

    @Bean
    fun kafkaTransactionManager(binderFactory: DefaultBinderFactory): KafkaTransactionManager<*, *> {
        val kafka = binderFactory.getBinder("kafka", MessageChannel::class.java) as KafkaMessageChannelBinder
        val transactionalProducerFactory = kafka.transactionalProducerFactory
        return KafkaTransactionManager(transactionalProducerFactory)
    }
}
