package com.github.sor2171.backend.controller

import com.github.sor2171.backend.entity.ApiResponse
import com.github.sor2171.backend.handler.v1.ExampleHandler
import com.github.sor2171.backend.service.WsTokenService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Mono

@RestController
@RequestMapping("/api/test")
class TestController(
    private val wsTokenService: WsTokenService
) {
    @GetMapping("/hello")
    fun test(): Mono<String> = Mono.just("Hello Kotlin WebFlux!")

    @GetMapping("/wstoken")
    fun testToken(): Mono<ApiResponse<String>> =
        wsTokenService.registerToken(ExampleHandler::class)
            .map { token ->
                ApiResponse.success(data = token)
            }
            .onErrorResume { e ->
                Mono.just(ApiResponse.innerError(e.message))
            }
}