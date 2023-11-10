package io.waketunes.application.port.`in`

import io.waketunes.application.domain.model.AggregateId
import io.waketunes.application.domain.model.TimelineContent
import io.waketunes.application.domain.model.TimelineContent.*

data class RemoveSongTimelineCommand(
    val timelineContentId: TimelineContentId,
    val timelineId: AggregateId
)
