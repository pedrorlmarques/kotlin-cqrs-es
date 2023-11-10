package io.waketunes.application.domain.model

import io.waketunes.application.domain.event.*
import io.waketunes.application.domain.model.TimelineContent.TimelineContentId
import java.util.*

class Timeline private constructor(id: AggregateId) : AggregateRoot(id) {

    companion object {
        fun createTimeline(id: UUID, content: TimelineContent): Timeline {
            return Timeline(AggregateId(id.toString())).apply {
                fireEvent(TimelineCreatedEvent(content))
            }
        }

        fun recreateTimeline(id: AggregateId, events: List<Event>): Timeline {
            return if (events.isEmpty()) {
                throw RuntimeException("Timeline doesn't exist")
            } else {
                Timeline(AggregateId(id.value)).apply { replayEvents(events) }
            }
        }
    }

    // properties
    val timelineContents: MutableList<TimelineContent> = mutableListOf()
    var active: Boolean = false


    fun addContent(content: TimelineContent) {
        require(active) { "Timeline doesn't exist" }
        require(timelineContents.isNotEmpty()) { "Timeline not created" }
        fireEvent(TimelineContentAddedEvent(content))
    }

    fun removeContent(id: TimelineContentId) {
        require(active) { "Timeline doesn't exist" }
        val content = timelineContents.firstOrNull { it.id == id }
            ?: throw RuntimeException("Content doesn't exist")

        fireEvent(TimelineContentRemovedEvent(content))
    }

    fun deleteTimeLine(id: AggregateId) {
        require(active) { "Timeline was already deleted" }
        fireEvent(TimelineDeletedEvent(id))
    }

    override fun handleEvent(event: Event) {
        when (event) {
            is TimelineCreatedEvent -> {
                active = true
                timelineContents.add(event.content)
            }

            is TimelineContentAddedEvent -> timelineContents.add(event.content)
            is TimelineContentRemovedEvent -> timelineContents.remove(event.content)
            is TimelineDeletedEvent -> {
                active = false
                timelineContents.clear()
            }
        }
    }
}

sealed class TimelineContent(var id: TimelineContentId) {
    data class SongTimelineContent(val value: Song) :
        TimelineContent(id = TimelineContentId(UUID.randomUUID().toString())) {
        override fun toString(): String {
            return "SongTimelineContent(" +
                    "id=$id, " +
                    "value=$value" +
                    ")"
        }
    }

    data class TimelineContentId(val value: String)
}
