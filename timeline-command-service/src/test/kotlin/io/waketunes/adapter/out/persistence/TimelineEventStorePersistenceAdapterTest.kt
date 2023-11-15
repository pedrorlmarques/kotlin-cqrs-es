package io.waketunes.adapter.out.persistence

import io.waketunes.TestTimelineCommandServiceApplication
import io.waketunes.application.domain.event.TimelineCreatedEvent
import io.waketunes.application.domain.model.*
import io.waketunes.application.domain.model.TimelineContent.SongTimelineContent
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest
import org.springframework.boot.testcontainers.properties.TestcontainersPropertySourceAutoConfiguration
import org.springframework.context.annotation.Import
import org.springframework.data.mongodb.core.MongoTemplate
import java.net.URL
import java.util.*

@DataMongoTest
@Import(value = [TimelineEventStorePersistenceAdapter::class, TestTimelineCommandServiceApplication::class, TestcontainersPropertySourceAutoConfiguration::class])
class TimelineEventStorePersistenceAdapterTest(
    @Autowired val timelineEventStorePersistenceAdapter: TimelineEventStorePersistenceAdapter,
    @Autowired val mongoTemplate: MongoTemplate
) {

    @Test
    fun testGivenAggregateIdItShouldReturnAllTheEvents() {

        val aggregateId = AggregateId("1")

        val timeLineCreatedEvent = TimelineCreatedEvent(
            SongTimelineContent(
                Song(
                    SongId(UUID.randomUUID().toString()),
                    SongName("yellow"),
                    SongSource(url = URL("http://localhost:8080"))
                )
            ), aggregateId
        )

        val expectedEvent = EventModel(
            null,
            Date(),
            aggregateId.value,
            Timeline::class.java.typeName,
            timeLineCreatedEvent::class.java.typeName,
            timeLineCreatedEvent
        )

        mongoTemplate.save(expectedEvent)

        assertThat(timelineEventStorePersistenceAdapter.load(aggregateId))
            .isNotEmpty
            .containsExactlyInAnyOrder(timeLineCreatedEvent)
    }
}
