package io.waketunes.application.domain.service

import io.waketunes.application.domain.model.Timeline
import io.waketunes.application.port.`in`.DeleteTimelineCommand
import io.waketunes.application.port.`in`.DeleteTimelineUseCase
import io.waketunes.application.port.out.LoadTimelineEventsPort
import io.waketunes.application.port.out.SaveTimelineEventsPort
import io.waketunes.application.port.out.SendTimelineEventsPort
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class DeleteTimelineService(
    val loadTimelineEventsPort: LoadTimelineEventsPort,
    val saveTimelineEventsPort: SaveTimelineEventsPort,
    val sendTimelineEventsPort: SendTimelineEventsPort
) : DeleteTimelineUseCase {
    override fun delete(command: DeleteTimelineCommand) =
        Timeline.recreateTimeline(command.timelineId, loadTimelineEventsPort.load(command.timelineId))
            .apply {
                deleteTimeLine(command.timelineId)
                saveTimelineEventsPort.save(id, uncommittedEvents())
                sendTimelineEventsPort.send(uncommittedEvents())
                markEventsAsCommitted()
            }
}
