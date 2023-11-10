package io.waketunes.application.port.out

import io.waketunes.application.domain.event.Event
import io.waketunes.application.domain.model.AggregateId

fun interface SaveTimelineEventsPort {
    fun save(aggregateId: AggregateId, uncommittedEvents: List<Event>)
}
