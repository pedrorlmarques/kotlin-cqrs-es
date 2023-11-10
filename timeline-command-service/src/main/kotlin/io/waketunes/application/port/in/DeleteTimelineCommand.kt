package io.waketunes.application.port.`in`

import io.waketunes.application.domain.model.AggregateId

data class DeleteTimelineCommand(val timelineId: AggregateId)
