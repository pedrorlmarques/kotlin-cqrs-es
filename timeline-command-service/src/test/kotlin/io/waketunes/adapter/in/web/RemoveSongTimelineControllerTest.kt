package io.waketunes.adapter.`in`.web

import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import io.waketunes.application.domain.model.AggregateId
import io.waketunes.application.domain.model.Timeline
import io.waketunes.application.domain.model.TimelineContent.TimelineContentId
import io.waketunes.application.port.`in`.RemoveSongTimelineCommand
import io.waketunes.application.port.`in`.RemoveSongTimelineUseCase
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.delete


@WebMvcTest(controllers = [RemoveSongTimelineController::class])
class RemoveSongTimelineControllerTest(@Autowired val mockMvc: MockMvc) {

    @MockkBean
    lateinit var removeSongTimelineUseCase: RemoveSongTimelineUseCase

    @Test
    fun `given timelineContentId and timelineId it should remove the content from timeline`() {

        val timelineId = "123"
        val timelineContentId = "1234"

        val command = RemoveSongTimelineCommand(
            TimelineContentId(
                timelineContentId
            ), AggregateId(timelineId)
        )

        val expectedTimeLine: Timeline = mockk()

        every { removeSongTimelineUseCase.remove(command) } returns expectedTimeLine

        mockMvc.delete("/api/song-timeline/{timelineId}/content/{contentId}", timelineId, timelineContentId)
            .andExpect {
                status {
                    isNoContent()
                }
            }

        verify { removeSongTimelineUseCase.remove(command) }
    }

}
