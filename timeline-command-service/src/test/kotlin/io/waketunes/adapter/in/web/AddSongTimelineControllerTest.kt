package io.waketunes.adapter.`in`.web

import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import io.mockk.mockk
import io.waketunes.application.domain.model.*
import io.waketunes.application.port.`in`.AddSongTimelineCommand
import io.waketunes.application.port.`in`.AddSongTimelineUseCase
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.put
import java.net.URL
import java.util.*


@WebMvcTest(controllers = [AddSongTimelineController::class])
class AddSongTimelineControllerTest(@Autowired val mockMvc: MockMvc) {

    @MockkBean
    lateinit var addSongTimelineUseCase: AddSongTimelineUseCase

    @Test
    fun testGivenSongRequestToAddToATimelineItShouldAdd() {

        val song = Song(
            SongId(UUID.randomUUID().toString()), SongName("yellow"), SongSource(url = URL("http://localhost:8080"))
        )

        val timelineId = UUID.randomUUID().toString()

        val expectedTimeLine: Timeline = mockk()

        every {
            addSongTimelineUseCase.add(AddSongTimelineCommand(AggregateId(timelineId), song))
        } returns expectedTimeLine

        mockMvc.put("/api/song-timeline/{timelineId}/add-song", timelineId) {
            contentType = MediaType.APPLICATION_JSON
            content =
                "{\"content\":{\"id\":\"${song.id.value}\",\"name\":\"${song.name.value}\",\"url\":\"${song.source.url}\"}}"
        }.andExpect {
            status {
                isNoContent()
            }
        }
    }
}
