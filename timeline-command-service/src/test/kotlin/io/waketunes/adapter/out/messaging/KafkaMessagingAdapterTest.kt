package io.waketunes.adapter.out.messaging

import io.waketunes.TestTimelineCommandServiceApplication
import io.waketunes.TimelineCommandServiceApplication
import io.waketunes.application.domain.event.TimelineCreatedEvent
import io.waketunes.application.domain.model.*
import io.waketunes.application.domain.model.TimelineContent.SongTimelineContent
import org.apache.kafka.clients.consumer.KafkaConsumer
import org.assertj.core.api.Assertions.assertThat
import org.awaitility.kotlin.await
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.kafka.test.utils.KafkaTestUtils
import org.testcontainers.containers.KafkaContainer
import java.net.URL
import java.time.Duration
import java.util.*

@SpringBootTest(classes = [TimelineCommandServiceApplication::class, TestTimelineCommandServiceApplication::class])
class KafkaMessagingAdapterTest(
    @Autowired val kafkaMessagingAdapter: KafkaMessagingAdapter,
    @Autowired val kafkaContainer: KafkaContainer
) {

    @Test
    fun testGivenNonCommittedEventsItShouldSendToKafka() {

        val timelineId = AggregateId("1")
        val song = Song(
            SongId(UUID.randomUUID().toString()),
            SongName("yellow"),
            SongSource(url = URL("http://localhost:8080"))
        )

        val yellowOneContent = SongTimelineContent(song)

        val nonCommittedEvents = mutableListOf(TimelineCreatedEvent(yellowOneContent, timelineId))

        kafkaMessagingAdapter.send(nonCommittedEvents)

        val kafkaConsumer = createConsumer(mutableListOf("domain-event.topic"))

        await.untilAsserted {
            val consumerRecords = kafkaConsumer.poll(Duration.ofMillis(100))
            assertThat(consumerRecords.count()).isOne()
            val first = consumerRecords.first()
            assertThat(first).isNotNull
            assertThat(first.key()).isNull()
            assertThat(first.value()).isNotNull
        }
    }

    private fun createConsumer(topics: List<String>): KafkaConsumer<String, String> = KafkaConsumer<String, String>(
        KafkaTestUtils.consumerProps(
            kafkaContainer.bootstrapServers,
            UUID.randomUUID().toString(),
            "true"
        )
    ).apply { subscribe(topics) }
}
