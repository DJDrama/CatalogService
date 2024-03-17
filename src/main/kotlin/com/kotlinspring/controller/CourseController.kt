package com.kotlinspring.controller

import com.kotlinspring.dto.CourseDto
import com.kotlinspring.service.CourseService
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/v1/courses")
@Validated
class CourseController(
    private val courseService: CourseService
) {

    @GetMapping
    fun retrieveAllCourses(): List<CourseDto> {
        return courseService.retrieveAllCourses()
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun addCourse(@RequestBody @Valid courseDto: CourseDto): CourseDto {
        return courseService.addCourse(courseDto = courseDto)
    }

    @PutMapping("/{courseId}")
    fun updateCourse(@PathVariable("courseId") courseId: Int, @RequestBody courseDto: CourseDto): CourseDto {
        return courseService.updateCourse(courseId, courseDto)
    }

    @PostMapping("/{courseId}/delete")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun deleteCourse(@PathVariable("courseId") courseId: Int){
        return courseService.deleteCourse(courseId)
    }

}