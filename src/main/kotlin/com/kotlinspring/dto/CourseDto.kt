package com.kotlinspring.dto

import com.kotlinspring.entity.Course

data class CourseDto(
    val id: Int?, // generated automatically
    val name: String,
    val category: String,
)

fun CourseDto.asEntityModel(): Course {
    return Course(id = this.id, name = this.name, category = this.category)
}

fun List<CourseDto>.asEntityList(): List<Course> {
    return map { it.asEntityModel() }
}
