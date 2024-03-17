package com.kotlinspring.repository

import com.kotlinspring.util.courseEntityList
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.test.context.ActiveProfiles
import java.util.stream.Stream

@DataJpaTest
@ActiveProfiles("test")
class CourseRepositoryIntgTest {

    @Autowired
    lateinit var courseRepository: CourseRepository

    @BeforeEach
    fun setup() {
        courseRepository.deleteAll()
        val courses = courseEntityList()
        courseRepository.saveAll(courses)
    }

    @Test
    fun findByNameContaining(){
        val foundCourses = courseRepository.findByNameContaining(courseName = "SpringBoot")
        Assertions.assertEquals(foundCourses.size, 2)
    }

    @Test
    fun findCoursesByName(){
        val foundCourses = courseRepository.findCoursesByName(courseName = "SpringBoot")
        Assertions.assertEquals(foundCourses.size, 2)
    }

    @ParameterizedTest
    @MethodSource("courseAndSize")
    fun findCoursesByName_approach2(name: String, expectedSize: Int){
        val foundCourses = courseRepository.findCoursesByName(name)
        Assertions.assertEquals(foundCourses.size, expectedSize)
    }

    companion object {
        @JvmStatic
        fun courseAndSize(): Stream<Arguments> {
            return Stream.of(
                Arguments.arguments("SpringBoot", 2),
                Arguments.arguments("Wiremock", 1),
            )
        }
    }

}