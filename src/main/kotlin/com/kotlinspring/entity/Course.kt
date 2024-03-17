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
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="instructor_id", nullable = false)
    val instructor: Instructor? = null
) {
    override fun toString(): String {
        return "Course(id=$id, name=$name, category=$category, instructor=${instructor!!.id}"
    }
}

fun Course.asDtoModel(): CourseDto {
    return CourseDto(id = this.id, name = this.name, category = this.category)
}

fun List<Course>.asDtoList(): List<CourseDto> = map {
    it.asDtoModel()
}