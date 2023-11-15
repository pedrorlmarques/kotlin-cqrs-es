package io.waketunes.application.domain.service

import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import io.mockk.verify
import io.waketunes.application.domain.event.TimelineContentAddedEvent
import io.waketunes.application.domain.event.TimelineCreatedEvent
import io.waketunes.application.domain.model.*
import io.waketunes.application.port.`in`.DeleteTimelineCommand
import io.waketunes.application.port.out.LoadTimelineEventsPort
import io.waketunes.application.port.out.SaveTimelineEventsPort
import io.waketunes.application.port.out.SendTimelineEventsPort
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import java.net.URL
import java.util.*

@ExtendWith(MockKExtension::class)
class DeleteTimelineServiceTest {


    @MockK
    lateinit var loadTimelineEventsPort: LoadTimelineEventsPort

    @MockK
    lateinit var saveTimelineEventsPort: SaveTimelineEventsPort

    @MockK
    lateinit var sendTimelineEventsPort: SendTimelineEventsPort


    @Test
    fun testGivenTimelineIdItShouldMarkAsInactiveAndClearAllContent() {

        val timelineId = AggregateId("1")
        val command = DeleteTimelineCommand(timelineId)

        val song = Song(
            SongId(UUID.randomUUID().toString()),
            SongName("yellow"),
            SongSource(url = URL("http://localhost:8080"))
        )

        val songTimelineContent = TimelineContent.SongTimelineContent(song)

        val existingEvents = mutableListOf(
            TimelineCreatedEvent(songTimelineContent, command.timelineId),
            TimelineContentAddedEvent(songTimelineContent, command.timelineId),
        )

        every { loadTimelineEventsPort.load(command.timelineId) } returns existingEvents
        every { saveTimelineEventsPort.save(any(), any()) } returns Unit
        every { sendTimelineEventsPort.send(any()) } returns Unit

        val timeline = DeleteTimelineService(
            loadTimelineEventsPort,
            saveTimelineEventsPort,
            sendTimelineEventsPort
        ).delete(command)

        assertThat(timeline).isNotNull
        assertThat(timeline.active).isFalse
        assertThat(timeline.timelineContents).isEmpty()
        assertThat(timeline.uncommittedEvents()).isEmpty()

        verify { loadTimelineEventsPort.load(command.timelineId) }
        verify { saveTimelineEventsPort.save(any(), any()) }
        verify { sendTimelineEventsPort.send(any()) }
    }
}
