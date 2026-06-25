package com.github.sor2171.backend.exception

import com.github.sor2171.backend.entity.ApiResponse
import jakarta.validation.ValidationException
import org.slf4j.LoggerFactory
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class ValidationController {

    private val log = LoggerFactory.getLogger(ValidationController::class.java)

    @ExceptionHandler(ValidationException::class)
    fun validationException(e: Exception): ApiResponse<out String?> {
        log.warn("Resolve [${e.javaClass.name}: ${e.message}]")
        return ApiResponse.failure(
            400,
            null,
            "parameter validation error"
        )
    }
}