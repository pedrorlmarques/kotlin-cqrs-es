package io.waketunes.adapter.`in`.web

import io.waketunes.application.domain.model.AggregateId
import io.waketunes.application.port.`in`.DeleteTimelineCommand
import io.waketunes.application.port.`in`.DeleteTimelineUseCase
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/song-timeline")
internal class DeleteTimelineController(val deleteTimelineUseCase: DeleteTimelineUseCase) {

    @DeleteMapping("/{timelineId}/")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun remove(@PathVariable timelineId: String) {
        deleteTimelineUseCase.delete(DeleteTimelineCommand(AggregateId(timelineId)))
    }
}
