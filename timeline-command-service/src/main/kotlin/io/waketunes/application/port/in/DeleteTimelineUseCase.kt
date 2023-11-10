package io.waketunes.application.port.`in`

import io.waketunes.application.domain.model.Timeline

fun interface DeleteTimelineUseCase {
    fun delete(command: DeleteTimelineCommand): Timeline
}
