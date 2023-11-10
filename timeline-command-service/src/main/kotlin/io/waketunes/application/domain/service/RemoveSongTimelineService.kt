package io.waketunes.application.domain.service

import io.waketunes.application.domain.model.Timeline
import io.waketunes.application.port.`in`.RemoveSongTimelineCommand
import io.waketunes.application.port.`in`.RemoveSongTimelineUseCase
import io.waketunes.application.port.out.LoadTimelineEventsPort
import io.waketunes.application.port.out.SaveTimelineEventsPort
import io.waketunes.application.port.out.SendTimelineEventsPort
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
internal class RemoveSongTimelineService(
    val loadTimelineEventsPort: LoadTimelineEventsPort,
    val saveTimelineEventsPort: SaveTimelineEventsPort,
    val sendTimelineEventsPort: SendTimelineEventsPort
) : RemoveSongTimelineUseCase {

    override fun remove(command: RemoveSongTimelineCommand) =
        Timeline.recreateTimeline(command.timelineId, loadTimelineEventsPort.load(command.timelineId))
            .apply {
                removeContent(command.timelineContentId)
                saveTimelineEventsPort.save(id, uncommittedEvents())
                sendTimelineEventsPort.send(uncommittedEvents())
                markEventsAsCommitted()
            }
}
