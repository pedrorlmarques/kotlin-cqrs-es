package io.waketunes.application.port.`in`

import io.waketunes.application.domain.model.Song

data class CreateSongTimelineCommand(val content: Song)
