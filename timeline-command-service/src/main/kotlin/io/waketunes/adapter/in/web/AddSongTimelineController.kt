package io.waketunes.adapter.`in`.web

import io.waketunes.application.domain.model.AggregateId
import io.waketunes.application.port.`in`.AddSongTimelineCommand
import io.waketunes.application.port.`in`.AddSongTimelineUseCase
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*


@RestController
@RequestMapping("/api/song-timeline")
internal class AddSongTimelineController(val addSongTimelineUseCase: AddSongTimelineUseCase) {

    @PutMapping("/{timelineId}/add-song")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun add(@RequestBody request: AddSongTimelineRequest, @PathVariable timelineId: String) {
        addSongTimelineUseCase.add(request.toAddSongTimelineCommand(timelineId))
    }
}

internal data class AddSongTimelineRequest(val content: SongRequest)

private fun AddSongTimelineRequest.toAddSongTimelineCommand(timelineId: String) =
    AddSongTimelineCommand(AggregateId(timelineId), content.toSong())
