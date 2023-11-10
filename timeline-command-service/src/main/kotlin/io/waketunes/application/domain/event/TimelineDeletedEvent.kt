package io.waketunes.application.domain.event

import io.waketunes.application.domain.model.AggregateId

data class TimelineDeletedEvent(val timelineId: AggregateId) : Event()
