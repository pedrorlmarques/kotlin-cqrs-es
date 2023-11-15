package io.waketunes.application.domain.service

import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import io.mockk.verify
import io.waketunes.application.domain.event.TimelineContentAddedEvent
import io.waketunes.application.domain.event.TimelineCreatedEvent
import io.waketunes.application.domain.model.*
import io.waketunes.application.domain.model.TimelineContent.SongTimelineContent
import io.waketunes.application.domain.model.TimelineContent.TimelineContentId
import io.waketunes.application.port.`in`.RemoveSongTimelineCommand
import io.waketunes.application.port.out.LoadTimelineEventsPort
import io.waketunes.application.port.out.SaveTimelineEventsPort
import io.waketunes.application.port.out.SendTimelineEventsPort
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import java.net.URL
import java.util.*

@ExtendWith(MockKExtension::class)
class RemoveSongTimelineServiceTest {

    @MockK
    lateinit var loadTimelineEventsPort: LoadTimelineEventsPort

    @MockK
    lateinit var saveTimelineEventsPort: SaveTimelineEventsPort

    @MockK
    lateinit var sendTimelineEventsPort: SendTimelineEventsPort

    @Test
    fun testGivenRemoveSongTimelineCommandItShouldRemoveContent() {

        val song = Song(
            SongId(UUID.randomUUID().toString()),
            SongName("yellow"),
            SongSource(url = URL("http://localhost:8080"))
        )

        val timelineId = AggregateId("1")
        val yellowOneContent = SongTimelineContent(song)
        val yellowTwoContent = SongTimelineContent(song)


        val existingEvents = mutableListOf(
            TimelineCreatedEvent(yellowOneContent, timelineId),
            TimelineContentAddedEvent(yellowTwoContent, timelineId),
        )

        val command = RemoveSongTimelineCommand(TimelineContentId(yellowOneContent.id.value), timelineId)

        every { loadTimelineEventsPort.load(command.timelineId) } returns existingEvents
        every { saveTimelineEventsPort.save(any(), any()) } returns Unit
        every { sendTimelineEventsPort.send(any()) } returns Unit


        val timeline =
            RemoveSongTimelineService(loadTimelineEventsPort, saveTimelineEventsPort, sendTimelineEventsPort).remove(
                command
            )

        assertThat(timeline).isNotNull
        assertThat(timeline.active).isTrue
        assertThat(timeline.timelineContents)
            .hasSize(1)
            .containsExactly(yellowTwoContent)
        assertThat(timeline.uncommittedEvents()).isEmpty()

        verify { loadTimelineEventsPort.load(command.timelineId) }
        verify { saveTimelineEventsPort.save(any(), any()) }
        verify { sendTimelineEventsPort.send(any()) }
    }
}
