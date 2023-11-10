package io.waketunes.application.domain.model

import java.net.URL

data class Song(val id: SongId, val name: SongName, val source: SongSource)

data class SongSource(val system: String = "spotify", val url: URL)

data class SongName(val value: String)

data class SongId(val value: String)
