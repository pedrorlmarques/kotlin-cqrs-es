package io.waketunes.application.domain.model

import io.waketunes.application.domain.event.Event

abstract class AggregateRoot(val id: AggregateId) {

    private val uncommittedEvents = mutableListOf<Event>()

    protected fun fireEvent(event: Event) = with(event) {
        handleEvent(this)
        uncommittedEvents.add(this)
    }

    protected abstract fun handleEvent(event: Event)

    protected fun replayEvents(events: List<Event>) = events.forEach { handleEvent(it) }

    fun uncommittedEvents() = uncommittedEvents

    fun markEventsAsCommitted() = uncommittedEvents.clear()

}

data class AggregateId(val value: String)
