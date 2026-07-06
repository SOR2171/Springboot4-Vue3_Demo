package com.github.sor2171.backend.service

import com.github.sor2171.backend.utils.Const.WS_REQUEST_TOKEN
import org.springframework.beans.factory.annotation.Value
import org.springframework.data.redis.core.ReactiveStringRedisTemplate
import org.springframework.stereotype.Service
import org.springframework.web.reactive.socket.WebSocketHandler
import org.springframework.web.reactive.socket.WebSocketSession
import org.springframework.web.util.UriComponentsBuilder
import reactor.core.publisher.Mono
import java.time.Duration
import java.util.*
import kotlin.reflect.KClass

@Service
class WsTokenService(
    private val template: ReactiveStringRedisTemplate,

    @param:Value($$"${app.ws.token-expire-time}")
    private val defaultTokenExpireTime: Long,
) {
    fun registerToken(
        wsType: KClass<out WebSocketHandler>,
        expireTime: Long? = null,
    ): Mono<String> {
        val token = UUID.randomUUID().toString()
        val key = "$WS_REQUEST_TOKEN:${wsType.simpleName}:" + UUID.randomUUID().toString()
        return template.opsForValue().set(
            key,
            "1",
            Duration.ofSeconds(expireTime ?: defaultTokenExpireTime)
        ).flatMap { success ->
            if (success) {
                Mono.just(token)
            } else {
                Mono.error(IllegalStateException("Failed to register token"))
            }
        }
    }

    fun validateToken(
        session: WebSocketSession,
        wsType: KClass<out WebSocketHandler>,
    ): Mono<Boolean> {
        val token = UriComponentsBuilder
            .fromUri(session.handshakeInfo.uri)
            .build()
            .queryParams
            .getFirst("token")
            ?: return Mono.just(false)

        val key = "$WS_REQUEST_TOKEN:${wsType.simpleName}:" + token
        return template.delete(key).map { it > 0 }
    }
}