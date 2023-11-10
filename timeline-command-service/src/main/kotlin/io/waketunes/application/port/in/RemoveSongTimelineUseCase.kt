package io.waketunes.application.port.`in`

import io.waketunes.application.domain.model.Timeline

fun interface RemoveSongTimelineUseCase {
    fun remove(command: RemoveSongTimelineCommand): Timeline
}
