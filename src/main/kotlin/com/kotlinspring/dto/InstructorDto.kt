package com.kotlinspring.dto

import com.kotlinspring.entity.Instructor
import jakarta.validation.constraints.NotBlank

data class InstructorDto(
    val id: Int?,
    @get:NotBlank(message = "instructorDto.category must not be blank")
    val name: String
)

fun InstructorDto.asEntityModel(): Instructor {
    return Instructor(id = this.id, name = this.name)
}