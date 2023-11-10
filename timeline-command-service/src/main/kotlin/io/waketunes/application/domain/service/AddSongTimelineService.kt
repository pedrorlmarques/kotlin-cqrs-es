package io.waketunes.application.domain.service

import io.waketunes.application.domain.model.Timeline
import io.waketunes.application.domain.model.TimelineContent.SongTimelineContent
import io.waketunes.application.port.`in`.AddSongTimelineCommand
import io.waketunes.application.port.`in`.AddSongTimelineUseCase
import io.waketunes.application.port.out.LoadTimelineEventsPort
import io.waketunes.application.port.out.SaveTimelineEventsPort
import io.waketunes.application.port.out.SendTimelineEventsPort
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
internal class AddSongTimelineService(
    val loadTimelineEventsPort: LoadTimelineEventsPort,
    val saveTimelineEventsPort: SaveTimelineEventsPort,
    val sendTimelineEventsPort: SendTimelineEventsPort
) : AddSongTimelineUseCase {

    override fun add(command: AddSongTimelineCommand) =
        Timeline.recreateTimeline(command.timelineId, loadTimelineEventsPort.load(command.timelineId))
            .apply {
                addContent(SongTimelineContent(command.content))
                saveTimelineEventsPort.save(id, uncommittedEvents())
                sendTimelineEventsPort.send(uncommittedEvents())
                markEventsAsCommitted()
            }
}
