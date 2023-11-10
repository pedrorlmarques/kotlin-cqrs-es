package io.waketunes.adapter.`in`.web

import io.waketunes.application.domain.model.AggregateId
import io.waketunes.application.domain.model.TimelineContent.TimelineContentId
import io.waketunes.application.port.`in`.RemoveSongTimelineCommand
import io.waketunes.application.port.`in`.RemoveSongTimelineUseCase
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/song-timeline")
internal class RemoveSongTimelineController(val removeSongTimelineUseCase: RemoveSongTimelineUseCase) {

    @DeleteMapping("/{timelineId}/content/{contentId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun remove(@PathVariable timelineId: String, @PathVariable contentId: String) {
        removeSongTimelineUseCase.remove(
            RemoveSongTimelineCommand(
                TimelineContentId(contentId),
                AggregateId(timelineId)
            )
        )
    }
}
