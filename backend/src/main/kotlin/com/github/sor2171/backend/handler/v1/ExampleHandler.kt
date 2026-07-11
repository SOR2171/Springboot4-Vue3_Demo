package com.github.sor2171.backend.handler.v1

import com.github.sor2171.backend.handler.WsHandler
import com.github.sor2171.backend.service.WsTokenService
import org.springframework.stereotype.Component
import org.springframework.web.reactive.socket.WebSocketHandler
import org.springframework.web.reactive.socket.WebSocketSession
import reactor.core.publisher.Mono

@Component
@WsHandler("/ws/v1/example")
class ExampleHandler(
    private val wsTokenService: WsTokenService,
) : WebSocketHandler {
    override fun handle(session: WebSocketSession): Mono<Void> {
        val output = session.receive()
            .map { msg ->
                val userText = msg.payloadAsText
                session.textMessage("Kotlin springboot4 received: $userText")
            }

        return wsTokenService
            .validateToken(session, this::class)
            .flatMap { valid ->
                if (!valid) {
                    session.close()
                } else {
                    session.send(output)
                }
            }
    }
}
