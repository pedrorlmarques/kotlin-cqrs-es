package io.waketunes.application.port.out

import io.waketunes.application.domain.event.Event
import io.waketunes.application.domain.model.AggregateId

fun interface LoadTimelineEventsPort {
    fun load(id: AggregateId): List<Event>
}
