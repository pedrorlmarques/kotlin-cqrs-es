package io.waketunes.adapter.`in`.web

import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import io.waketunes.application.domain.model.AggregateId
import io.waketunes.application.domain.model.Timeline
import io.waketunes.application.port.`in`.DeleteTimelineCommand
import io.waketunes.application.port.`in`.DeleteTimelineUseCase
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.delete
import java.util.*

@WebMvcTest(controllers = [DeleteTimelineController::class])
class DeleteTimelineControllerTest(@Autowired val mockMvc: MockMvc) {

    @MockkBean
    lateinit var deleteTimelineUseCase: DeleteTimelineUseCase

    @Test
    fun testGivenTimelineIdItShouldDeleteTimeline() {

        val timelineId = UUID.randomUUID().toString()
        val expectedTimeLine: Timeline = mockk()

        every {
            deleteTimelineUseCase.delete(DeleteTimelineCommand(AggregateId(timelineId)))
        } returns expectedTimeLine

        mockMvc.delete("/api/song-timeline/{timelineId}/", timelineId)
            .andExpect {
                status {
                    isNoContent()
                }
            }

        verify { deleteTimelineUseCase.delete(DeleteTimelineCommand(AggregateId(timelineId))) }
    }
}
