package io.waketunes.application.domain.service

import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import io.mockk.verify
import io.waketunes.application.domain.model.Song
import io.waketunes.application.domain.model.SongId
import io.waketunes.application.domain.model.SongName
import io.waketunes.application.domain.model.SongSource
import io.waketunes.application.domain.model.TimelineContent.SongTimelineContent
import io.waketunes.application.port.`in`.CreateSongTimelineCommand
import io.waketunes.application.port.out.SaveTimelineEventsPort
import io.waketunes.application.port.out.SendTimelineEventsPort
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import java.net.URL
import java.util.*

@ExtendWith(MockKExtension::class)
class CreateSongTimelineServiceTest {

    @MockK
    lateinit var saveTimelineEventsPort: SaveTimelineEventsPort

    @MockK
    lateinit var sendTimelineEventsPort: SendTimelineEventsPort

    @Test
    fun testGivenCreateSongTimelineCommandItShouldCreateTimeLine() {

        val song = Song(
            SongId(UUID.randomUUID().toString()),
            SongName("yellow"),
            SongSource(url = URL("http://localhost:8080"))
        )

        every { saveTimelineEventsPort.save(any(), any()) } returns Unit
        every { sendTimelineEventsPort.send(any()) } returns Unit

        val timeline = CreateSongTimelineService(saveTimelineEventsPort, sendTimelineEventsPort).create(
            CreateSongTimelineCommand(song)
        )

        val expectedSongTimelineContent = SongTimelineContent(song)

        assertThat(timeline).isNotNull
        assertThat(timeline.active).isTrue
        assertThat(timeline.timelineContents).hasSize(1).containsExactly(expectedSongTimelineContent)
        assertThat(timeline.uncommittedEvents()).isEmpty()
        verify { saveTimelineEventsPort.save(timeline.id, timeline.uncommittedEvents()) }
        verify { sendTimelineEventsPort.send(any()) }
    }
}
