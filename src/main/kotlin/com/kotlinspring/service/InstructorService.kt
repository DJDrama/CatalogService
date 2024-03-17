package com.kotlinspring.service

import com.kotlinspring.dto.InstructorDto
import com.kotlinspring.dto.asEntityModel
import com.kotlinspring.entity.Instructor
import com.kotlinspring.entity.asDtoModel
import com.kotlinspring.repository.InstructorRepository
import org.springframework.stereotype.Service
import java.util.*

@Service
class InstructorService(
    private val instructorRepository: InstructorRepository
) {

    fun createInstructor(instructorDto: InstructorDto): InstructorDto {
        val instructor = instructorDto.asEntityModel()
        val savedInstructor = instructorRepository.save(instructor)
        return savedInstructor.asDtoModel()
    }

    fun findByInstructorId(instructorId: Int): Optional<Instructor> {
        return instructorRepository.findById(instructorId)
    }

}
