package com.kotlinspring.entity

import com.kotlinspring.dto.CourseDto
import jakarta.persistence.*


@Entity
@Table(name = "Course")
data class Course(
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    val id: Int?,
    val name: String,
    val category: String,
)

fun Course.asDtoModel(): CourseDto {
    return CourseDto(id = this.id, name = this.name, category = this.category)
}

fun List<Course>.asDtoList(): List<CourseDto> = map{
    it.asDtoModel()
}