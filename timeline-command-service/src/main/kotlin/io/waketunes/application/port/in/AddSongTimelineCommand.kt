package io.waketunes.application.port.`in`

import io.waketunes.application.domain.model.AggregateId
import io.waketunes.application.domain.model.Song

data class AddSongTimelineCommand(val timelineId: AggregateId, val content: Song)
