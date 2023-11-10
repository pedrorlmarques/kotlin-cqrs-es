package io.waketunes.adapter.out.persistence

import org.springframework.data.mongodb.repository.MongoRepository

interface EventStoreRepository : MongoRepository<EventModel, String> {
    fun findAllByAggregateIdentifierOrderByCreatedDateAsc(aggregateIdentifier: String): List<EventModel>
}
