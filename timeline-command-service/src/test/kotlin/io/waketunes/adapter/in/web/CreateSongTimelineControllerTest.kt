package io.waketunes.adapter.`in`.web

import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import io.mockk.mockk
import io.waketunes.application.domain.model.*
import io.waketunes.application.port.`in`.CreateSongTimelineCommand
import io.waketunes.application.port.`in`.CreateSongTimelineUseCase
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.post
import java.net.URL
import java.util.*


@WebMvcTest(controllers = [CreateSongTimelineController::class])
class CreateSongTimelineControllerTest(@Autowired val mockMvc: MockMvc) {

    @MockkBean
    lateinit var createSongTimelineUseCase: CreateSongTimelineUseCase

    @Test
    fun testGivenSongRequestItShouldCreateTimeLine() {

        val song = Song(
            SongId(UUID.randomUUID().toString()), SongName("yellow"), SongSource(url = URL("http://localhost:8080"))
        )

        val expectedTimeLine: Timeline = mockk()

        every {
            createSongTimelineUseCase.create(CreateSongTimelineCommand(song))
        } returns expectedTimeLine

        mockMvc.post("/api/song-timeline") {
            contentType = MediaType.APPLICATION_JSON
            content =
                "{\"content\":{\"id\":\"${song.id.value}\",\"name\":\"${song.name.value}\",\"url\":\"${song.source.url}\"}}"
        }.andExpect {
            status {
                isCreated()
            }
        }
    }
}
