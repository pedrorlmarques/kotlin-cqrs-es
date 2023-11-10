package io.waketunes.application.port.out

import io.waketunes.application.domain.event.Event

fun interface SendTimelineEventsPort {
    fun send(events: List<Event>)
}
