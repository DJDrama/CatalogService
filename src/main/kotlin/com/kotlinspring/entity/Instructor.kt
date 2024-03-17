package com.kotlinspring.entity

import com.kotlinspring.dto.InstructorDto
import jakarta.persistence.*

@Entity
@Table(name="Instructor")
data class Instructor (
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    val id: Int?,
    val name: String,
    @OneToMany(
        mappedBy = "instructor",
        cascade = [CascadeType.ALL],
        orphanRemoval = true
    )
    val courses: List<Course> = emptyList()

)

fun Instructor.asDtoModel(): InstructorDto {
    return InstructorDto(id = id, name = name)
}