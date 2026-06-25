package com.github.sor2171.backend.config

import com.github.sor2171.backend.handler.TestHandler
import com.github.sor2171.backend.handler.v1.ExampleHandler
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.reactive.HandlerMapping
import org.springframework.web.reactive.handler.SimpleUrlHandlerMapping
import org.springframework.web.reactive.socket.server.support.WebSocketHandlerAdapter

@Configuration
class WebSocketConfig(
    private val exampleHandler: ExampleHandler,
    private val testHandler: TestHandler
) {
    @Bean
    fun handlerMapping(): HandlerMapping {
        val map = mapOf(
            "/ws/v1/example" to exampleHandler,
            "/ws/test" to testHandler
        )
        val mapping = SimpleUrlHandlerMapping()
        mapping.urlMap = map
        mapping.order = 1
        return mapping
    }

    @Bean
    fun handlerAdapter(): WebSocketHandlerAdapter =
        WebSocketHandlerAdapter()
}