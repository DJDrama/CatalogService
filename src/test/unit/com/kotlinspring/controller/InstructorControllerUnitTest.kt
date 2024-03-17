package com.kotlinspring.controller

import com.kotlinspring.dto.InstructorDto
import com.kotlinspring.service.InstructorService
import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.test.web.reactive.server.WebTestClient
import org.springframework.test.web.reactive.server.expectBody

@WebMvcTest(controllers = [InstructorController::class])
@AutoConfigureWebTestClient
class InstructorControllerUnitTest {


    @Autowired
    lateinit var webTestClient: WebTestClient

    @MockkBean
    lateinit var instructorService: InstructorService

    @Test
    fun addInstructor() {
        val instructorDto = InstructorDto(null, "DJ")
        every { instructorService.createInstructor(instructorDto = instructorDto) } returns InstructorDto(1, "DJ")
        val savedInstructorDto = webTestClient
            .post()
            .uri("/v1/instructors")
            .bodyValue(instructorDto)
            .exchange()
            .expectStatus().isCreated
            .expectBody<InstructorDto>()
            .returnResult()
            .responseBody

        Assertions.assertTrue {
            savedInstructorDto?.id != null
        }
    }

    @Test
    fun addInstructor_Validation(){
        val instructorDto = InstructorDto(null, "")

        every { instructorService.createInstructor(instructorDto) } returns InstructorDto(1, "")
        val response = webTestClient
            .post()
            .uri("/v1/instructors")
            .bodyValue(instructorDto)
            .exchange()
            .expectStatus().isBadRequest
            .expectBody<String>()
            .returnResult()
            .responseBody

        Assertions.assertEquals("instructorDto.category must not be blank", response)
    }
}