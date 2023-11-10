package io.waketunes.adapter.`in`.web

import io.waketunes.application.domain.model.Song
import io.waketunes.application.domain.model.SongId
import io.waketunes.application.domain.model.SongName
import io.waketunes.application.domain.model.SongSource
import io.waketunes.application.port.`in`.CreateSongTimelineCommand
import io.waketunes.application.port.`in`.CreateSongTimelineUseCase
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import java.net.URL

@RestController
@RequestMapping("/api/song-timeline")
internal class CreateSongTimelineController(val createSongTimelineUseCase: CreateSongTimelineUseCase) {

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun create(@RequestBody request: CreateSongTimelineRequest) {
        createSongTimelineUseCase.create(request.toCreateTimelineCommand())
    }
}

internal data class CreateSongTimelineRequest(val content: SongRequest)
internal data class SongRequest(val id: String, val name: String, val url: URL)

internal fun SongRequest.toSong(): Song = Song(SongId(id), SongName(name), SongSource("spotify", url))

private fun CreateSongTimelineRequest.toCreateTimelineCommand(): CreateSongTimelineCommand =
    CreateSongTimelineCommand(content.toSong())


