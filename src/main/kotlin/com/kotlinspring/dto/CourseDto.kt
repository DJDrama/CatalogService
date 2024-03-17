package com.kotlinspring.dto

import com.kotlinspring.entity.Course
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull

data class CourseDto(
    val id: Int?, // generated automatically
    @get:NotBlank(message = "courseDto.name must not be blank")
    val name: String,
    @get:NotBlank(message = "courseDto.category must not be blank")
    val category: String,
    @get:NotNull(message = "courseDto.instructorId must not be null")
    val instructorId: Int? = null
)

fun CourseDto.asEntityModel(): Course {
    return Course(id = this.id, name = this.name, category = this.category)
}

fun List<CourseDto>.asEntityList(): List<Course> {
    return map { it.asEntityModel() }
}
