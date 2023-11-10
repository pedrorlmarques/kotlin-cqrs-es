package io.waketunes.application.port.`in`

import io.waketunes.application.domain.model.Timeline

fun interface CreateSongTimelineUseCase {
    fun create(command: CreateSongTimelineCommand): Timeline
}
