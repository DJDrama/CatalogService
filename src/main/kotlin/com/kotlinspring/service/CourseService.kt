package com.kotlinspring.service

import com.kotlinspring.dto.CourseDto
import com.kotlinspring.dto.asEntityModel
import com.kotlinspring.entity.asDtoModel
import com.kotlinspring.exception.CourseNotFoundException
import com.kotlinspring.exception.InstructorNotValidException
import com.kotlinspring.repository.CourseRepository
import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.stereotype.Service

@Service
class CourseService(
    private val courseRepository: CourseRepository,
    private val instructorService: InstructorService
) {
    private val logger = KotlinLogging.logger { }

    fun addCourse(courseDto: CourseDto): CourseDto {
        val instructor = instructorService.findByInstructorId(courseDto.instructorId!!)

        if(!instructor.isPresent)
            throw InstructorNotValidException("Instructor not valid for the Id: ${courseDto.instructorId}")

        val courseEntity = courseDto.asEntityModel().copy(instructor = instructor.get())
        val savedCourse = courseRepository.save(courseEntity)

        logger.info { "Saved course is : $savedCourse" }

        return savedCourse.asDtoModel().copy(instructorId = savedCourse.instructor!!.id)
    }

    fun retrieveAllCourses(courseName: String? = null): List<CourseDto> {
        val courses = courseName?.let {
            courseRepository.findByNameContaining(courseName = courseName)
        } ?: courseRepository.findAll()
        return courses.map {
            it.asDtoModel()
        }
    }

    fun updateCourse(courseId: Int, courseDto: CourseDto): CourseDto {
        val existingCourse = courseRepository.findById(courseId)
        if (existingCourse.isPresent) {
            val courseDtoForUpdate = existingCourse.get().copy(name = courseDto.name, category = courseDto.category)
            return courseRepository.save(courseDtoForUpdate).asDtoModel()
        } else {
            // throw
            throw CourseNotFoundException("Course of the id does not exist: $courseId")
        }
    }

    fun deleteCourse(courseId: Int) {
        val existingCourse = courseRepository.findById(courseId)
        if (existingCourse.isPresent) {
            courseRepository.deleteById(courseId)
        } else {
            // throw
            throw CourseNotFoundException("Course of the id does not exist: $courseId")
        }
    }

}