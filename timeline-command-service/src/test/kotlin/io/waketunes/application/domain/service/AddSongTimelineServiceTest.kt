package io.waketunes.application.domain.service

import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import io.mockk.verify
import io.waketunes.application.domain.event.TimelineContentAddedEvent
import io.waketunes.application.domain.event.TimelineCreatedEvent
import io.waketunes.application.domain.model.*
import io.waketunes.application.domain.model.TimelineContent.SongTimelineContent
import io.waketunes.application.port.`in`.AddSongTimelineCommand
import io.waketunes.application.port.out.LoadTimelineEventsPort
import io.waketunes.application.port.out.SaveTimelineEventsPort
import io.waketunes.application.port.out.SendTimelineEventsPort
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import java.net.URL
import java.util.*


@ExtendWith(MockKExtension::class)
class AddSongTimelineServiceTest {

    @MockK
    lateinit var loadTimelineEventsPort: LoadTimelineEventsPort

    @MockK
    lateinit var saveTimelineEventsPort: SaveTimelineEventsPort

    @MockK
    lateinit var sendTimelineEventsPort: SendTimelineEventsPort

    @Test
    fun testGivenAddSongCommandItShouldAddToTheTimeLine() {

        val song = Song(
            SongId(UUID.randomUUID().toString()),
            SongName("yellow"),
            SongSource(url = URL("http://localhost:8080"))
        )

        val songTimelineContent = SongTimelineContent(song)

        val existingEvents = mutableListOf(
            TimelineCreatedEvent(songTimelineContent),
            TimelineContentAddedEvent(songTimelineContent),
        )

        val command = AddSongTimelineCommand(AggregateId("1"), song)

        every { loadTimelineEventsPort.load(command.timelineId) } returns existingEvents
        every { saveTimelineEventsPort.save(any(), any()) } returns Unit
        every { sendTimelineEventsPort.send(any()) } returns Unit

        val timeline =
            AddSongTimelineService(loadTimelineEventsPort, saveTimelineEventsPort, sendTimelineEventsPort).add(command)

        assertThat(timeline).isNotNull
        assertThat(timeline.active).isTrue
        assertThat(timeline.timelineContents)
            .hasSize(3)
            .containsExactly(
                songTimelineContent,
                songTimelineContent,
                songTimelineContent
            )
        assertThat(timeline.uncommittedEvents()).isEmpty()

        verify { loadTimelineEventsPort.load(command.timelineId) }
        verify { saveTimelineEventsPort.save(any(), any()) }
        verify { sendTimelineEventsPort.send(any()) }
    }
}
