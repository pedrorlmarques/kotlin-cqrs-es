package io.waketunes.adapter.out.persistence

import io.waketunes.application.domain.event.Event
import io.waketunes.application.domain.model.AggregateId
import io.waketunes.application.domain.model.Timeline
import io.waketunes.application.port.out.LoadTimelineEventsPort
import io.waketunes.application.port.out.SaveTimelineEventsPort
import org.springframework.stereotype.Component
import java.util.*

@Component
class TimelineEventStorePersistenceAdapter(
    val eventStoreRepository: EventStoreRepository
) : SaveTimelineEventsPort,
    LoadTimelineEventsPort {

    override fun load(id: AggregateId): List<Event> =
        eventStoreRepository.findAllByAggregateIdentifierOrderByCreatedDateAsc(id.value)
            .mapNotNull { it.event }

    override fun save(aggregateId: AggregateId, uncommittedEvents: List<Event>) {
        uncommittedEvents.map {
            EventModel(null, Date(), aggregateId.value, Timeline::class.java.typeName, it.javaClass.typeName, it)
        }.also {
            eventStoreRepository.saveAll(it)
        }
    }
}
