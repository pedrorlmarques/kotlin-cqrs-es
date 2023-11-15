package io.waketunes.application.domain.event

import io.waketunes.application.domain.model.AggregateId
import io.waketunes.application.domain.model.TimelineContent.TimelineContentId

data class TimelineContentRemovedEvent(val timelineContentId: TimelineContentId, val timelineId: AggregateId) : Event()
