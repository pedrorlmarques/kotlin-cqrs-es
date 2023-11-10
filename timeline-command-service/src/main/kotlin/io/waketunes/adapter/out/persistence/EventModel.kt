package io.waketunes.adapter.out.persistence

import io.waketunes.application.domain.event.Event
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.util.*

@Document
data class EventModel(
    @Id val id: String?,
    val createdDate: Date,
    val aggregateIdentifier: String,
    val aggregateType: String,
    val eventType: String,
    val event: Event?
)
