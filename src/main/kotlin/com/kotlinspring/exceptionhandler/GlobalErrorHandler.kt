package com.kotlinspring.exceptionhandler

import com.kotlinspring.exception.InstructorNotValidException
import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.HttpStatusCode
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Component
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.context.request.WebRequest
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler

@Component
@ControllerAdvice
class GlobalErrorHandler : ResponseEntityExceptionHandler() {
    private val privateLogger = KotlinLogging.logger { }

    override fun handleMethodArgumentNotValid(
        ex: MethodArgumentNotValidException,
        headers: HttpHeaders,
        status: HttpStatusCode,
        request: WebRequest
    ): ResponseEntity<Any>? {
        //return super.handleMethodArgumentNotValid(ex, headers, status, request)
        privateLogger.error(ex) { "MethodArgumentNotValidException observed: ${ex.message}" }

        val errors = ex.bindingResult.allErrors
            .map { error ->
                error.defaultMessage!!
            }
            .sorted()
        logger.info { "Errors : $errors" }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body(errors.joinToString(", ") { it })
    }

    @ExceptionHandler(InstructorNotValidException::class)
    fun handleInstructorNotValidException(ex: InstructorNotValidException, request: WebRequest): ResponseEntity<Any> {
        privateLogger.error(ex) { "Exception observed: ${ex.message}" }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(ex.message)
    }

    @ExceptionHandler(Exception::class)
    fun handleAllException(ex: Exception, request: WebRequest): ResponseEntity<Any> {
        privateLogger.error(ex) { "Exception observed: ${ex.message}" }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(ex.message)
    }
}