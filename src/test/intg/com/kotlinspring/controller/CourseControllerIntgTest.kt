package com.kotlinspring.controller

import com.kotlinspring.dto.CourseDto
import com.kotlinspring.entity.Course
import com.kotlinspring.repository.CourseRepository
import com.kotlinspring.repository.InstructorRepository
import com.kotlinspring.util.courseEntityList
import com.kotlinspring.util.instructorEntity
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.reactive.server.WebTestClient
import org.springframework.web.util.UriComponentsBuilder

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@AutoConfigureWebTestClient
class CourseControllerIntgTest {

    @Autowired
    lateinit var webTestClient: WebTestClient

    @Autowired
    lateinit var courseRepository: CourseRepository

    @Autowired
    lateinit var instructorRepository: InstructorRepository

    @BeforeEach
    fun setup() {
        courseRepository.deleteAll()
        instructorRepository.deleteAll()

        val instructor = instructorEntity()
        instructorRepository.save(instructor)

        val courses = courseEntityList(instructor = instructor)
        courseRepository.saveAll(courses)
    }

    @Test
    fun addCourse() {
        val instructor = instructorRepository.findAll().first()
        val courseDto = CourseDto(
            id = null,
            name = "Build Restful APIs using Kotlin and SpringBoot",
            category = "Development",
            instructorId = instructor.id
        )

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
    fun retrieveAllCoursesByName() {
        val uri = UriComponentsBuilder.fromUriString("/v1/courses")
            .queryParam("courseName", "SpringBoot")
            .toUriString()

        val courseDtos = webTestClient
            .get()
            .uri(uri)
            .exchange()
            .expectStatus().isOk
            .expectBodyList(CourseDto::class.java)
            .returnResult()
            .responseBody

        Assertions.assertEquals(
            courseDtos?.size, 2
        )
    }

    @Test
    fun updateCourse() {
        val instructor = instructorRepository.findAll().first()

        // exsiting course
        val course = courseRepository.save(
            Course(
                id = null,
                name = "Build RestFul APis using SpringBoot and Kotlin",
                category = "Development",
                instructor = instructor
            )
        )

        // updated CourseDto
        val updatedCourseDto = CourseDto(
            id = null,
            name = "Build RestFul APis using SpringBoot and Kotlin1",
            category = "Development2",
            instructorId = course.instructor!!.id
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
        val instructor = instructorRepository.findAll().first()

        // exsiting course
        val course = courseRepository.save(
            Course(
                id = null,
                name = "Build RestFul APis using SpringBoot and Kotlin",
                category = "Development",
                instructor = instructor
            )
        )


        val deletedCourse = webTestClient
            .post()
            .uri("/v1/courses/{courseId}/delete", course.id)
            .exchange()
            .expectStatus().isNoContent
    }
}