package io.waketunes.adapter.out.messaging

import io.waketunes.application.domain.event.Event
import io.waketunes.application.port.out.SendTimelineEventsPort
import org.springframework.cloud.stream.function.StreamBridge
import org.springframework.messaging.support.MessageBuilder
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Component
class KafkaMessagingAdapter(val streamBridge: StreamBridge) : SendTimelineEventsPort {

    @Transactional
    override fun send(events: List<Event>) {
        events.forEach {
            streamBridge.send(
                "domainEventProducer",
                MessageBuilder
                    .withPayload(it)
                    .setHeader("event_type", it.javaClass.typeName)
                    .build()
            )
        }
    }
}
