package io.waketunes.application.domain.event

import io.waketunes.application.domain.model.TimelineContent

data class TimelineContentAddedEvent(val content: TimelineContent) : Event()
