package io.waketunes.application.domain.event

import io.waketunes.application.domain.model.AggregateId
import io.waketunes.application.domain.model.TimelineContent

data class TimelineContentAddedEvent(val content: TimelineContent, val timelineId: AggregateId) : Event()
