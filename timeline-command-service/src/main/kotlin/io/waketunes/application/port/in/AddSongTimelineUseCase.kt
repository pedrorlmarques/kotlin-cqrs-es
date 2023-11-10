package io.waketunes.application.port.`in`

import io.waketunes.application.domain.model.Timeline

fun interface AddSongTimelineUseCase {
    fun add(command: AddSongTimelineCommand): Timeline
}