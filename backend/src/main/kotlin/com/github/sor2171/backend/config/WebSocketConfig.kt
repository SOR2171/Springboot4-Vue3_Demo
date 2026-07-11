package com.github.sor2171.backend.config

import com.github.sor2171.backend.handler.WsHandler
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.reactive.HandlerMapping
import org.springframework.web.reactive.handler.SimpleUrlHandlerMapping
import org.springframework.web.reactive.socket.WebSocketHandler
import org.springframework.web.reactive.socket.server.support.WebSocketHandlerAdapter
import kotlin.reflect.full.findAnnotation

@Configuration
class WebSocketConfig(
    private val handlers: List<WebSocketHandler>
) {
    @Bean
    fun handlerMapping(): HandlerMapping {
        val map = handlers.associateBy {
            it::class.findAnnotation<WsHandler>()!!.path
        }
        return SimpleUrlHandlerMapping(map, 1)
    }

    @Bean
    fun handlerAdapter(): WebSocketHandlerAdapter =
        WebSocketHandlerAdapter()
}