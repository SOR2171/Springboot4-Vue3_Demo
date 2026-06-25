package com.github.sor2171.backend.controller.v1

import com.github.sor2171.backend.entity.ApiResponse
import com.github.sor2171.backend.entity.enums.EmailType
import com.github.sor2171.backend.entity.vo.request.EmailRegisterVO
import com.github.sor2171.backend.entity.vo.request.PasswordResetVO
import com.github.sor2171.backend.service.v1.AuthorizationService
import jakarta.validation.Valid
import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import org.slf4j.LoggerFactory
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*
import org.springframework.web.server.ServerWebExchange
import reactor.core.publisher.Mono

@Validated
@RestController
@RequestMapping("/api/v1/auth")
class AuthorizeController(
    private val service: AuthorizationService
) {
    private val logger = LoggerFactory.getLogger(this::class.java)

    @GetMapping("/ask-code")
    fun askVerifyCode(
        @RequestParam @NotBlank @Email email: String,
        @RequestParam type: EmailType,
        exchange: ServerWebExchange
    ): Mono<ApiResponse<Any?>> {
        logger.info("Trying to ask code")
        val ip = exchange.request.remoteAddress?.address?.hostAddress ?: "unknown"
        return service.askEmailVerifyCode(type, email, ip)
            .map(::messageHandler)
    }

    @PostMapping("/register")
    fun emailRegister(@RequestBody @Valid vo: EmailRegisterVO): Mono<ApiResponse<Any?>> {
        logger.info("Trying to register")
        return service.registerEmailAccount(vo)
            .map(::messageHandler)
    }

    @PostMapping("/reset")
    fun emailResetPassword(@RequestBody @Valid vo: PasswordResetVO): Mono<ApiResponse<Any?>> {
        logger.info("Trying to reset password")
        return service.resetEmailAccountPassword(vo)
            .map(::messageHandler)
    }

    @GetMapping("/logout")
    fun logout(exchange: ServerWebExchange): Mono<ApiResponse<Any?>> {
        logger.info("Trying to logout")
        val authorization = exchange.request.headers.getFirst("Authorization")
        return Mono.fromCallable { service.invalidateJwt(authorization) }
            .map { success ->
                if (success) ApiResponse.success()
                else ApiResponse.logoutFailed()
            }
    }

    @GetMapping("/relogin")
    fun relogin(exchange: ServerWebExchange): Mono<ApiResponse<Any?>> {
        logger.info("Trying to relogin")
        val authorization = exchange.request.headers.getFirst("Authorization")
        return service
            .jwtTokenRelogin(authorization)
            ?.map { ApiResponse.success(it) }
            ?: Mono.just(messageHandler("Please login with password."))
    }

    private fun messageHandler(wrongMessage: String): ApiResponse<Any?> {
        return if (wrongMessage.isBlank()) {
            ApiResponse.success()
        } else {
            ApiResponse.failure(400, null, wrongMessage)
        }
    }
}