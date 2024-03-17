package com.kotlinspring.controller

import com.kotlinspring.dto.CourseDto
import com.kotlinspring.entity.Course
import com.kotlinspring.entity.asDtoList
import com.kotlinspring.service.CourseService
import com.kotlinspring.util.courseDTO
import com.kotlinspring.util.courseEntityList
import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import io.mockk.just
import io.mockk.runs
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.test.web.reactive.server.WebTestClient
import org.springframework.test.web.reactive.server.expectBody

@WebMvcTest(controllers = [CourseController::class])
@AutoConfigureWebTestClient
class CourseControllerUnitTest {

    @Autowired
    lateinit var webTestClient: WebTestClient

    @MockkBean
    lateinit var courseServiceMock: CourseService

    @Test
    fun addCourse_validation() {
        val courseDto = CourseDto(
            id = null,
            name = "",
            category = "",
            instructorId = 1
        )

        every { courseServiceMock.addCourse(courseDto = courseDto) } returns courseDTO(id = 1)

        val response = webTestClient
            .post()
            .uri("/v1/courses")
            .bodyValue(courseDto)
            .exchange()
            .expectStatus().isBadRequest
            .expectBody(String::class.java)
            .returnResult()
            .responseBody
        Assertions.assertEquals("courseDto.category must not be blank, courseDto.name must not be blank", response)
    }

    @Test
    fun addCourse_runtimeException() {
        val courseDto = CourseDto(
            id = null,
            name = "Build Restful APIs using Kotlin and SpringBoot",
            category = "Development",
            instructorId = 1
        )

        val errorMessage = "Unexpected Error occurred."
        every { courseServiceMock.addCourse(courseDto = courseDto) } throws RuntimeException(errorMessage)

        val response = webTestClient
            .post()
            .uri("/v1/courses")
            .bodyValue(courseDto)
            .exchange()
            .expectStatus().is5xxServerError
            .expectBody(String::class.java)
            .returnResult()
            .responseBody
        Assertions.assertEquals(errorMessage, response)
    }

    @Test
    fun addCourse() {
        val courseDto = CourseDto(
            id = null,
            name = "Build Restful APIs using Kotlin and SpringBoot",
            category = "Development",
            instructorId = 1
        )

        every { courseServiceMock.addCourse(courseDto = courseDto) } returns courseDTO(id = 1)

        val savedCourseDto = webTestClient
            .post()
            .uri("/v1/courses")
            .bodyValue(courseDto)
            .exchange()
            .expectStatus().isCreated
            .expectBody(CourseDto::class.java)
            .returnResult()
            .responseBody

        Assertions.assertTrue {
            savedCourseDto?.id != null
        }
    }

    @Test
    fun retrieveAllCourses() {
        every { courseServiceMock.retrieveAllCourses() } returns courseEntityList().asDtoList()

        val courseDtos = webTestClient
            .get()
            .uri("/v1/courses")
            .exchange()
            .expectStatus().isOk
            .expectBodyList(CourseDto::class.java)
            .returnResult()
            .responseBody

        Assertions.assertEquals(
            courseDtos?.size, 3
        )
    }

    @Test
    fun updateCourse() {
        val courseDto = CourseDto(
            id = 1,
            name = "Build RestFul APis using SpringBoot and Kotlin",
            category = "Development",
            instructorId = 1
        )
        val updatedCourseDto = CourseDto(
            id = 1,
            name = "Build RestFul APis using SpringBoot and Kotlin1",
            category = "Development2",
            instructorId = 1
        )
        every {
            courseServiceMock.updateCourse(
                courseId = courseDto.id!!,
                courseDto = courseDto
            )
        } returns updatedCourseDto

        val updatedCourse = webTestClient
            .put()
            .uri("/v1/courses/{courseId}", courseDto.id)
            .bodyValue(courseDto)
            .exchange()
            .expectStatus().isOk
            .expectBody(CourseDto::class.java)
            .returnResult()
            .responseBody

        Assertions.assertEquals("Build RestFul APis using SpringBoot and Kotlin1", updatedCourse?.name)
        Assertions.assertEquals("Development2", updatedCourse?.category)
    }

    @Test
    fun deleteCourse() {
        val courseDtoForDelete = CourseDto(
            id = 1,
            name = "Build RestFul APis using SpringBoot and Kotlin",
            category = "Development"
        )
        every {
            courseServiceMock.deleteCourse(courseId = courseDtoForDelete.id!!)
        } just runs // returns nothing just run it.


        webTestClient
            .post()
            .uri("/v1/courses/{courseId}/delete", courseDtoForDelete.id)
            .exchange()
            .expectStatus().isNoContent
    }
}