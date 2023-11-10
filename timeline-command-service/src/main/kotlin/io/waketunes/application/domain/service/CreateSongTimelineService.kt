package io.waketunes.application.domain.service

import io.waketunes.application.domain.model.Timeline
import io.waketunes.application.domain.model.TimelineContent.SongTimelineContent
import io.waketunes.application.port.`in`.CreateSongTimelineCommand
import io.waketunes.application.port.`in`.CreateSongTimelineUseCase
import io.waketunes.application.port.out.SaveTimelineEventsPort
import io.waketunes.application.port.out.SendTimelineEventsPort
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Service
@Transactional
internal class CreateSongTimelineService(
    val saveTimelineEventsPort: SaveTimelineEventsPort,
    val sendTimelineEventsPort: SendTimelineEventsPort
) : CreateSongTimelineUseCase {

    override fun create(command: CreateSongTimelineCommand) =
        Timeline.createTimeline(UUID.randomUUID(), SongTimelineContent(command.content))
            .apply {
                saveTimelineEventsPort.save(id, uncommittedEvents())
                sendTimelineEventsPort.send(uncommittedEvents())
                markEventsAsCommitted()
            }
}
