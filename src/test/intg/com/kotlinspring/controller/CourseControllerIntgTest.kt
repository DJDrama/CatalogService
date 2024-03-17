package com.kotlinspring.controller

import com.kotlinspring.dto.CourseDto
import com.kotlinspring.entity.Course
import com.kotlinspring.repository.CourseRepository
import com.kotlinspring.util.courseEntityList
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.reactive.server.WebTestClient

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@AutoConfigureWebTestClient
class CourseControllerIntgTest {

    @Autowired
    lateinit var webTestClient: WebTestClient

    @Autowired
    lateinit var courseRepository: CourseRepository

    @BeforeEach
    fun setup() {
        courseRepository.deleteAll()
        val courses = courseEntityList()
        courseRepository.saveAll(courses)
    }

    @Test
    fun addCourse() {
        val courseDto = CourseDto(null, "Build Restful APIs using Kotlin and SpringBoot", "Development")

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
        // exsiting course
        val course = courseRepository.save(
            Course(
                null,
                "Build RestFul APis using SpringBoot and Kotlin", "Development"
            )
        )

        // updated CourseDto
        val updatedCourseDto = CourseDto(
            null,
            "Build RestFul APis using SpringBoot and Kotlin1", "Development2"
        )

        val updatedCourse = webTestClient
            .put()
            .uri("/v1/courses/{courseId}", course.id)
            .bodyValue(updatedCourseDto)
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
        // exsiting course
        val course = courseRepository.save(
            Course(
                null,
                "Build RestFul APis using SpringBoot and Kotlin", "Development"
            )
        )


        val deletedCourse = webTestClient
            .post()
            .uri("/v1/courses/{courseId}/delete", course.id)
            .exchange()
            .expectStatus().isNoContent
    }
}