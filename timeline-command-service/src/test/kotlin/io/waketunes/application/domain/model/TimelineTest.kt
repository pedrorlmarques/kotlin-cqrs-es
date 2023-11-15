package io.waketunes.application.domain.model


import io.waketunes.application.domain.event.TimelineCreatedEvent
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.net.URL
import java.util.*

class TimelineTest {

    @Test
    fun testGivenSongContentItShouldCreateSongTimeLineWithContentAndTimeLineCreatedEventIsFired() {

        val timeLineIdValue = UUID.randomUUID()
        val timelineId = AggregateId(timeLineIdValue.toString())

        val songTimeLineContent = TimelineContent.SongTimelineContent(
            Song(
                SongId(UUID.randomUUID().toString()),
                SongName("I love you"),
                SongSource(url = URL("http://localhost:8080"))
            )
        )

        val songTimeLine = Timeline.createTimeline(timeLineIdValue, songTimeLineContent)

        assertThat(songTimeLine).isNotNull
        assertThat(songTimeLine.uncommittedEvents()).isNotEmpty.containsExactly(
            TimelineCreatedEvent(songTimeLineContent, timelineId)
        )
        assertThat(songTimeLine.timelineContents).isNotEmpty.containsExactly(songTimeLineContent)
    }
}
